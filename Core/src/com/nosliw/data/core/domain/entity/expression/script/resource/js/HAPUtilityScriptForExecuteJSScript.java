package com.nosliw.data.core.domain.entity.expression.script.resource.js;

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
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableExpression;
import com.nosliw.data.core.domain.entity.expression.script.HAPExecutableSegmentExpression;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.runtime.js.rhino.task.HAPInfoRuntimeTaskScript;

public class HAPUtilityScriptForExecuteJSScript {

	private static Map<String, HAPSegmentScriptProcessor> m_processors = new LinkedHashMap<String, HAPSegmentScriptProcessor>();

	private static String functionsParmName = "functions"; 
	private static String expressionsDataParmName = "expressionsData"; 
	private static String constantsDataParmName = "constantsData"; 
	private static String variablesDataParmName = "variablesData"; 
	
	static {
		m_processors.put(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPT, new HAPSegmentScriptProcessorScript());
		m_processors.put(HAPConstantShared.EXPRESSION_SEG_TYPE_DATA, new HAPSegmentScriptProcessorData());
		m_processors.put(HAPConstantShared.EXPRESSION_SEG_TYPE_DATASCRIPT, new HAPSegmentScriptProcessorDataScript());
		m_processors.put(HAPConstantShared.EXPRESSION_SEG_TYPE_TEXT, new HAPSegmentScriptProcessorText());
	}
	
	public static HAPScriptFunctionInfo buildExpressionFunctionInfo(HAPExecutableExpression expressionExe) {
		HAPScriptFunctionInfo out = new HAPScriptFunctionInfo();

		List<HAPExecutableSegmentExpression> segments = expressionExe.getSegments();
		for(int i=0; i<segments.size(); i++) {
			HAPExecutableSegmentExpression segment = segments.get(i);
			HAPScriptFunctionInfo segmentScript = buildExpressionSegmentFunctionInfo(segment);
			out.mergeWith(segmentScript);
		}
		
		StringBuffer funScript = buildSegmentFunctionScript(segments);
		if(expressionExe.getType().equals(HAPConstantShared.EXPRESSION_TYPE_LITERATE))  funScript.append("+\"\"");
		out.setMainScript(HAPJSScriptInfo.buildByScript(funScript.toString(), null));
		return out;
	}
	
	public static StringBuffer buildSegmentFunctionScript(List<HAPExecutableSegmentExpression> segments) {
		StringBuffer funScript = new StringBuffer();
		for(int i=0; i<segments.size(); i++) {
			HAPExecutableSegmentExpression segment = segments.get(i);
			funScript.append(functionsParmName+"[\""+segment.getId()+"\"]("+functionsParmName+", "+expressionsDataParmName+", "+constantsDataParmName+", "+variablesDataParmName+")");
			if(i<segments.size()-1)   funScript.append("+");
		}
		return funScript;
	}
	
	private static HAPScriptFunctionInfo buildExpressionSegmentFunctionInfo(HAPExecutableSegmentExpression expressionSegment) {
		HAPScriptFunctionInfo out = new HAPScriptFunctionInfo();

		String segmentType = expressionSegment.getType();
		HAPSegmentScriptProcessor scriptProcessor = m_processors.get(segmentType);
		HAPOutputScriptProcessor output = scriptProcessor.processor(expressionSegment, functionsParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);

		String functionStr = HAPUtilityScriptForExecuteJSScript.buildFunction(output.getFunctionBody(), functionsParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);
		out.setMainScript(HAPJSScriptInfo.buildByScript(functionStr, expressionSegment.getId()));
		 
		List<HAPExecutableSegmentExpression> children = output.getScriptChildren();
		for(HAPExecutableSegmentExpression childScript : children) {
			HAPScriptFunctionInfo childScriptFunInfo = buildExpressionSegmentFunctionInfo(childScript);
			out.mergeWith(childScriptFunInfo);
		}
		
		return out;
	}

	//build script for execute script expression task 
	public static HAPJSScriptInfo buildRequestScriptForExecuteScriptTask(HAPInfoRuntimeTaskScript taskInfo, HAPRuntimeTask task, HAPRuntimeImpRhino runtime){
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