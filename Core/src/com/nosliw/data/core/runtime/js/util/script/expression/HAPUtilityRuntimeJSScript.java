package com.nosliw.data.core.runtime.js.util.script.expression;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskExpression;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;

public class HAPUtilityRuntimeJSScript {

	public static HAPJSScriptInfo buildRequestScriptForExecuteExpressionTask(HAPInfoRuntimeTaskExpression taskInfo, HAPRuntimeTask task, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("expression", HAPJsonUtility.formatJson(HAPSerializeManager.getInstance().toStringValue(taskInfo.getExpression(), HAPSerializationFormat.JSON)));
		templateParms.put("variables", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(taskInfo.getVariablesValue()==null?new LinkedHashMap<String, HAPData>() : taskInfo.getVariablesValue(), HAPSerializationFormat.JSON)));
		templateParms.put("references", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(taskInfo.getReferencesValue()==null?new LinkedHashMap<String, HAPData>() : taskInfo.getReferencesValue(), HAPSerializationFormat.JSON)));
		templateParms.put("constants", "{}");
		templateParms.put("itemName", taskInfo.getItemName());
		
		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", task.getTaskId());
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);
		
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ExecuteExpressionScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, task.getTaskId());
		return out;
	}
	
}
