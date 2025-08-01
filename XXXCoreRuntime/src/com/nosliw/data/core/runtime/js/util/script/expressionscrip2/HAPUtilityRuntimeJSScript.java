package com.nosliw.data.core.runtime.js.util.script.expressionscrip2;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.resource.imp.js.HAPJSScriptInfo;
import com.nosliw.core.runtime.HAPRuntimeTask;
import com.nosliw.core.runtime.js.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.core.runtime.js.rhino.HAPRuntimeImpRhino;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskScriptExpressionGroup;

public class HAPUtilityRuntimeJSScript {

	public static HAPJSScriptInfo buildRequestScriptForExecuteExpressionTask(HAPInfoRuntimeTaskScriptExpressionGroup taskInfo, HAPRuntimeTask task, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		
		templateParms.put("expressionGroupId", taskInfo.getExpressionGroupResourceId());
		templateParms.put("expressionItemId", taskInfo.getItemName());
		
		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", task.getTaskId());
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);
		
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ExecuteScriptRequest.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, task.getTaskId());
		return out;
	}
	
}
