package com.nosliw.core.application.common.scriptexpression.serialize;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonTypeAsItIs;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.common.scriptexpression.HAPManualExpressionScript;
import com.nosliw.core.application.common.scriptexpression.HAPManualSegmentScriptExpression;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeImpRhino;

public class HAPUtilityScriptForExecuteJSScript {

	private static Map<String, HAPSegmentScriptProcessor> m_processors = new LinkedHashMap<String, HAPSegmentScriptProcessor>();

	private static String functionsParmName = "functions"; 
	private static String expressionsDataParmName = "expressionsData"; 
	private static String constantsDataParmName = "constantsData"; 
	private static String variablesDataParmName = "variablesData"; 
	
	static {
		m_processors.put(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTSIMPLE, new HAPSegmentScriptProcessorScriptSimple());
		m_processors.put(HAPConstantShared.EXPRESSION_SEG_TYPE_DATAEXPRESSION, new HAPSegmentScriptProcessorDataExpression());
		m_processors.put(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTCOMPLEX, new HAPSegmentScriptProcessorScriptComplex());
		m_processors.put(HAPConstantShared.EXPRESSION_SEG_TYPE_TEXT, new HAPSegmentScriptProcessorText());
	}
	
	public static HAPScriptFunctionInfo buildExpressionFunctionInfo(HAPManualExpressionScript expressionExe) {
		HAPScriptFunctionInfo out = new HAPScriptFunctionInfo();

		List<HAPManualSegmentScriptExpression> segments = expressionExe.getSegments();
		for(int i=0; i<segments.size(); i++) {
			HAPManualSegmentScriptExpression segment = segments.get(i);
			HAPScriptFunctionInfo segmentScript = buildExpressionSegmentFunctionInfo(segment);
			out.mergeWith(segmentScript);
		}
		
		StringBuffer funScript = buildSegmentFunctionScript(segments);
		if(expressionExe.getType().equals(HAPConstantShared.EXPRESSION_TYPE_LITERATE)) {
			funScript.append("+\"\"");
		}
		
		String script = HAPUtilityScriptForExecuteJSScript.buildFunction(funScript.toString(), functionsParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);
		out.setMainScript(HAPJSScriptInfo.buildByScript(script, null));
		return out;
	}
	
	public static StringBuffer buildSegmentFunctionScript(List<HAPManualSegmentScriptExpression> segments) {
		StringBuffer funScript = new StringBuffer();
		for(int i=0; i<segments.size(); i++) {
			HAPManualSegmentScriptExpression segment = segments.get(i);
			funScript.append(functionsParmName+"[\""+segment.getId()+"\"]("+functionsParmName+", "+expressionsDataParmName+", "+constantsDataParmName+", "+variablesDataParmName+")");
			if(i<segments.size()-1) {
				funScript.append("+");
			}
		}
		return funScript;
	}
	
	private static HAPScriptFunctionInfo buildExpressionSegmentFunctionInfo(HAPManualSegmentScriptExpression expressionSegment) {
		HAPScriptFunctionInfo out = new HAPScriptFunctionInfo();

		String segmentType = expressionSegment.getType();
		HAPSegmentScriptProcessor scriptProcessor = m_processors.get(segmentType);
		HAPOutputScriptProcessor output = scriptProcessor.processor(expressionSegment, functionsParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);

		String functionStr = HAPUtilityScriptForExecuteJSScript.buildFunction(output.getFunctionBody(), functionsParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);
		out.setMainScript(HAPJSScriptInfo.buildByScript(functionStr, expressionSegment.getId()));
		 
		List<HAPManualSegmentScriptExpression> children = output.getScriptChildren();
		for(HAPManualSegmentScriptExpression childScript : children) {
			HAPScriptFunctionInfo childScriptFunInfo = buildExpressionSegmentFunctionInfo(childScript);
			out.mergeWith(childScriptFunInfo);
		}
		
		return out;
	}

	//build script for execute script expression task 
	public static HAPJSScriptInfo buildRequestScriptForExecuteScriptTask(HAPInfoRuntimeTaskScriptExpressionGroup taskInfo, HAPRuntimeTask task, HAPRuntimeImpRhino runtime){
		Map<String, Object> variableValue = taskInfo.getVariablesValue();

		Map<String, String> templateParms = new LinkedHashMap<String, String>();

		templateParms.put("variables", HAPUtilityJson.formatJson(HAPUtilityJson.buildJson(variableValue==null?new LinkedHashMap<String, HAPData>() : variableValue, HAPSerializationFormat.JSON)));

		//build javascript function to execute the script
		HAPScriptFunctionInfo scriptFunctionInfo = taskInfo.getScriptFunction();
		templateParms.put("functionScript", scriptFunctionInfo.getMainScript().getScript());

		//functions
		String functionParmValue = "{}";
		List<HAPJSScriptInfo> childrenFun = scriptFunctionInfo.getChildren();
		if(!childrenFun.isEmpty()) {
			Map<String, String> funScriptMap = new LinkedHashMap<String, String>();
			Map<String, Class<?>> funScriptTypeMap = new LinkedHashMap<String, Class<?>>();
			for(HAPJSScriptInfo childFun : childrenFun) {
				funScriptMap.put(childFun.getName(), childFun.getScript());
				funScriptTypeMap.put(childFun.getName(), HAPJsonTypeAsItIs.class);
			}
			functionParmValue = HAPUtilityJson.buildMapJson(funScriptMap, funScriptTypeMap);
		}
		templateParms.put("functions", functionParmValue);
		
		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("expressions", HAPUtilityJson.formatJson(HAPUtilityJson.buildJson(taskInfo.getExpressionItems(), HAPSerializationFormat.JSON)));
		templateParms.put("taskId", task.getTaskId());
		templateParms.put("constants", HAPUtilityJson.buildJson(taskInfo.getConstantsValue(), HAPSerializationFormat.JSON));

		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);

		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityScriptForExecuteJSScript.class, "ExecuteScriptRequest.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, task.getTaskId());
		return out;
	}

	private static String buildFunction(
			String content,
			String functionsParmName,
			String expressionsDataParmName,
			String constantsDataParmName,
			String variablesDataParmName
	) {
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityScriptForExecuteJSScript.class, "ScriptFunction.temp");
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("functionScript", content);
		templateParms.put("functions", functionsParmName);
		templateParms.put("expressionsData", expressionsDataParmName);
		templateParms.put("constantsData", constantsDataParmName);
		templateParms.put("variablesData", variablesDataParmName);
		String out = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		return out;
	}
}
