package com.nosliw.data.core.runtime.js.util.expression;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.runtime.HAPRuntimeTaskExecuteExpression;
import com.nosliw.data.core.runtime.js.HAPJSScriptInfo;
import com.nosliw.data.core.runtime.js.rhino.HAPGatewayRhinoTaskResponse;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpRhino;

public class HAPUtilityRuntimeJSScript {

	public static HAPJSScriptInfo buildRequestScriptForExecuteExpressionTask(HAPRuntimeTaskExecuteExpression executeExpressionTask, HAPRuntimeImpRhino runtime){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("expression", HAPJsonUtility.formatJson(HAPSerializeManager.getInstance().toStringValue(executeExpressionTask.getExpression(), HAPSerializationFormat.JSON)));
		templateParms.put("variables", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(executeExpressionTask.getVariablesValue()==null?new LinkedHashMap<String, HAPData>() : executeExpressionTask.getVariablesValue(), HAPSerializationFormat.JSON)));
		templateParms.put("references", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(executeExpressionTask.getReferencesValue()==null?new LinkedHashMap<String, HAPData>() : executeExpressionTask.getReferencesValue(), HAPSerializationFormat.JSON)));
		templateParms.put("constants", "{}");
		templateParms.put("itemName", executeExpressionTask.getItemName());
		
		templateParms.put("successCommand", HAPGatewayRhinoTaskResponse.COMMAND_SUCCESS);
		templateParms.put("errorCommand", HAPGatewayRhinoTaskResponse.COMMAND_ERROR);
		templateParms.put("exceptionCommand", HAPGatewayRhinoTaskResponse.COMMAND_EXCEPTION);
		
		templateParms.put("gatewayId", runtime.getTaskResponseGatewayName());
		templateParms.put("parmTaskId", HAPGatewayRhinoTaskResponse.PARM_TASKID);
		templateParms.put("taskId", executeExpressionTask.getTaskId());
		templateParms.put("parmResponseData", HAPGatewayRhinoTaskResponse.PARM_RESPONSEDATA);
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPUtilityRuntimeJSScript.class, "ExecuteExpressionScript.temp");
		String script = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		HAPJSScriptInfo out = HAPJSScriptInfo.buildByScript(script, executeExpressionTask.getTaskId());
		return out;
	}
	
}
