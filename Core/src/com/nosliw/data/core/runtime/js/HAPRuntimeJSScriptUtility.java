package com.nosliw.data.core.runtime.js;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPExpressionTask;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpJSRhino;

public class HAPRuntimeJSScriptUtility {

	public static List<HAPJSScriptInfo> buildScriptForResource(HAPResource resource){
		List<HAPJSScriptInfo> out = new ArrayList<HAPJSScriptInfo>();
		//build library script info first
		if(resource.getId().getType().equals(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY)){
			out.addAll(buildScriptInfoForLibrary(resource));
		}
		
		StringBuffer script = new StringBuffer();
		if(HAPRuntimeImpJSRhino.ADDTORESOURCEMANAGER.equals(resource.getInfo().getValue(HAPRuntimeImpJSRhino.ADDTORESOURCEMANAGER)))  return out;
		
		//build script for resource data
		String valueScript = null;
		if(resource.getResourceData() instanceof HAPResourceDataJSValue){
			valueScript = ((HAPResourceDataJSValue)resource.getResourceData()).getValue();
		}
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put(HAPResourceId.ID, resource.getId().getId());
		templateParms.put(HAPResourceId.TYPE, resource.getId().getType());
		
		String infoJson = HAPSerializeManager.getInstance().toStringValue(resource.getInfo(), HAPSerializationFormat.JSON);
		if(HAPBasicUtility.isStringEmpty(infoJson)){
			templateParms.put(HAPResource.INFO, "undefined");
		}
		else{
			templateParms.put(HAPResource.INFO, infoJson);
		}

		if(valueScript==null)  valueScript = "undefined";
		templateParms.put(HAPResourceDataJSValue.VALUE, valueScript);

		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "ResourceScript.temp");
		script.append("\n");
		String resoruceDataScript = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		script.append(resoruceDataScript);
		script.append("\n");
		
		out.add(HAPJSScriptInfo.buildByScript(script.toString(), "Resource__"+resource.getId().toStringValue(HAPSerializationFormat.LITERATE)));
		return out;
	}
	
	private static List<HAPJSScriptInfo> buildScriptInfoForLibrary(HAPResource resource){
		List<HAPJSScriptInfo> out = new ArrayList<HAPJSScriptInfo>();
		
		HAPResourceDataJSLibrary resourceLibrary = (HAPResourceDataJSLibrary)resource.getResourceData();
		List<URI> uris = resourceLibrary.getURIs();
		for(URI uri : uris){
			File file = new File(uri);
			out.add(HAPJSScriptInfo.buildByFile(file.toString(), file.getName()));
		}
		return out;
	}

	public static String buildScriptForExecuteExpression(HAPExpressionTask expressionTask){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("expression", HAPJsonUtility.formatJson(HAPSerializeManager.getInstance().toStringValue(expressionTask.getExpression(), HAPSerializationFormat.JSON)));
		templateParms.put("variables", HAPJsonUtility.formatJson(HAPJsonUtility.buildJson(expressionTask.getVariablesValue()==null?new LinkedHashMap<String, HAPData>() : expressionTask.getVariablesValue(), HAPSerializationFormat.JSON)));
		templateParms.put("taskId", expressionTask.getTaskId());
		templateParms.put("gatewayPath", HAPConstant.RUNTIME_LANGUAGE_JS_GATEWAY);
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "ExecuteExpressionScript.temp");
		String out = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		return out;
	}
}
