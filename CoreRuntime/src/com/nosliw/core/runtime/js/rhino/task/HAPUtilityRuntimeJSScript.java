package com.nosliw.core.runtime.js.rhino.task;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.script.HAPJSScriptInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.runtime.js.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.core.resource.infrastructure.HAPRuntimeTaskLoadResources;
import com.nosliw.core.runtime.js.rhino.HAPExecutorRuntimeImpRhino;

public class HAPUtilityRuntimeJSScript {

	public static HAPJSScriptInfo buildRequestScriptForLoadResourceTask(HAPRuntimeTaskLoadResources loadResourcesTask, HAPExecutorRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("gatewayId", HAPConstantShared.GATEWAY_RHINOTASKRESPONSE);
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", loadResourcesTask.getTaskId());
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);

		templateParms.put("resourceInfos", HAPUtilityJson.formatJson(HAPUtilityJson.buildJson(loadResourcesTask.getResourcesInfo(), HAPSerializationFormat.JSON)));
		
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "LoadResources.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, loadResourcesTask.getTaskId());
		return out;
	}
	

	public static HAPJSScriptInfo buildTaskRequestScriptForExecuteExpressionScriptConstant(HAPInfoRuntimeTaskTaskScriptExpressionConstantGroup taskInfo, String taskId, HAPExecutorRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		
		templateParms.put("taskInfo", taskInfo.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		
		buildCommonTemplateParms(templateParms, taskId, runtime);
		
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ScriptExecuteScriptExpressionConstantGroup.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, taskId);
		return out;
	}
	
	public static HAPJSScriptInfo buildTaskRequestScriptForExecuteTaskGroupItemResource(String resourceType, String resourceId, String itemId, String taskId, HAPExecutorRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		
		templateParms.put("resourceType", resourceType);
		templateParms.put("resourceId", resourceId);
		templateParms.put("itemId", itemId);
		
		buildCommonTemplateParms(templateParms, taskId, runtime);
		
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ScriptExecuteResourceTaskGroupItem.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, taskId);
		return out;
	}
	
	public static HAPJSScriptInfo buildTaskRequestScriptForExecuteTaskResource(String resourceType, String resourceId, String taskId, HAPExecutorRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		
		templateParms.put("resourceType", resourceType);
		templateParms.put("resourceId", resourceId);
		
		buildCommonTemplateParms(templateParms, taskId, runtime);
		
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ScriptExecuteResourceTask.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, taskId);
		return out;
	}


	public static HAPJSScriptInfo buildTaskRequestScriptForExecuteTaskEntity(HAPExecutableBundle bundle, HAPPath mainEntityPath, String taskId, HAPExecutorRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		
		templateParms.put("bundleDefinition", bundle.toResourceData(runtime.getRuntimeInfo()).toString());
		if(mainEntityPath==null||mainEntityPath.isEmpty()) {
			templateParms.put("mainEntityPath", "");
		} else {
			templateParms.put("mainEntityPath", mainEntityPath.getPath());
		}
		
		buildCommonTemplateParms(templateParms, taskId, runtime);
		
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ScriptExecuteEntityTask.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, taskId);
		return out;
	}

	private static void buildCommonTemplateParms(Map<String, String> templateParms, String taskId, HAPExecutorRuntimeImpRhino runtime) {
		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("gatewayId", HAPConstantShared.GATEWAY_RHINOTASKRESPONSE);
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", taskId);
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);
	}
	
}
