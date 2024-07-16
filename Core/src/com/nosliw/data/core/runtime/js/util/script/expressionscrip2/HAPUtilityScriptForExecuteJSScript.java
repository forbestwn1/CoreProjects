package com.nosliw.data.core.runtime.js.util.script.expressionscrip2;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonTypeAsItIs;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskScriptExpressionGroup;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.script.expression1.HAPExecutableScript;

public class HAPUtilityScriptForExecuteJSScript {

	private static Map<String, HAPScriptProcessor> m_processors = new LinkedHashMap<String, HAPScriptProcessor>();

	private static String expressionsDataParmName = "expressionsData"; 
	private static String constantsDataParmName = "constantsData"; 
	private static String variablesDataParmName = "variablesData"; 
	private static String functionsParmName = "functions"; 
	
	static {
		m_processors.put(HAPConstantShared.EXPRESSION_TYPE_SCRIPT, new HAPScriptProcessorExpression());
		m_processors.put(HAPConstantShared.EXPRESSION_TYPE_LITERATE, new HAPScriptProcessorLiterate());
		m_processors.put(HAPConstantShared.EXPRESSION_SEG_TYPE_SCRIPTCOMPLEX, new HAPScriptProcessorExpression());
	}
	
	public static HAPScriptFunctionInfo buildFunctionInfo(HAPExecutableScript scriptExe) {
		HAPScriptFunctionInfo out = new HAPScriptFunctionInfo();
		
		String scriptType = scriptExe.getScriptType();
		HAPScriptProcessor scriptProcessor = m_processors.get(scriptType);
		HAPOutputScriptProcessor output = scriptProcessor.processor(scriptExe, functionsParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);
		
		String functionStr = HAPUtilityScriptForExecuteJSScript.buildFunction(output.getFunctionBody(), functionsParmName, expressionsDataParmName, constantsDataParmName, variablesDataParmName);
		out.setMainScript(HAPJSScriptInfo.buildByScript(functionStr, scriptExe.getId()));
		 
		List<HAPExecutableScript> children = output.getScriptChildren();
		for(HAPExecutableScript childScript : children) {
			HAPScriptFunctionInfo childScriptFunInfo = buildFunctionInfo(childScript);
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
