package com.nosliw.data.core.runtime.js.util.script;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.domain.HAPExecutableBundle;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.data.core.runtime.js.imp.rhino.HAPRuntimeImpRhino;

public class HAPUtilityRuntimeJSScript {

	public static HAPJSScriptInfo buildTaskRequestScriptForExecuteTaskGroupItemResource(String resourceType, String resourceId, String itemId, String taskId, HAPRuntimeImpRhino runtime){
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
	
	public static HAPJSScriptInfo buildTaskRequestScriptForExecuteTaskResource(String resourceType, String resourceId, String taskId, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		
		templateParms.put("resourceType", resourceType);
		templateParms.put("resourceId", resourceId);
		
		buildCommonTemplateParms(templateParms, taskId, runtime);
		
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ScriptExecuteResourceTask.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, taskId);
		return out;
	}


	public static HAPJSScriptInfo buildTaskRequestScriptForExecuteTaskEntity(HAPExecutableBundle bundle, HAPIdEntityInDomain mainEntityId, String taskId, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		
		templateParms.put("bundleDefinition", bundle.toResourceData(runtime.getRuntimeInfo()).toString());
		templateParms.put("entityType", mainEntityId.getEntityType());
		templateParms.put("entityId", mainEntityId.getEntityId());
		
		buildCommonTemplateParms(templateParms, taskId, runtime);
		
		InputStream javaTemplateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ScriptExecuteEntityTask.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, taskId);
		return out;
	}

	private static void buildCommonTemplateParms(Map<String, String> templateParms, String taskId, HAPRuntimeImpRhino runtime) {
		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", taskId);
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);
	}
	
}