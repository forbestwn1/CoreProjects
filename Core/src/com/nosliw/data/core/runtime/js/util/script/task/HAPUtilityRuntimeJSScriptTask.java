package com.nosliw.data.core.runtime.js.util.script.task;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.runtime.HAPInfoRuntimeTaskTask;
import com.nosliw.data.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeImpRhino;

public class HAPUtilityRuntimeJSScriptTask {

	public static HAPJSScriptInfo buildRequestScript(HAPInfoRuntimeTaskTask taskInfo, HAPRuntimeTask task, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();

		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("taskSuiteDef", taskInfo.getTaskSuite().toResourceData(runtime.getRuntimeInfo()).toString());
		templateParms.put("itemId", taskInfo.getItemName());
		templateParms.put("inputData", HAPUtilityJson.buildJson(taskInfo.getInputValue(), HAPSerializationFormat.JSON));
		
		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", task.getTaskId());
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);

		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScriptTask.class, "ExecuteTaskScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, task.getTaskId());
		return out;
	}
}
