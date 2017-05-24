package com.nosliw.data.core.runtime.js;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.expression.HAPExpression;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeImpJSRhino;
import com.nosliw.data.core.runtime.js.rhino.ScriptTracker;

public class HAPRuntimeJSScriptUtility {

	public static String buildScriptForResource(HAPResource resource, ScriptTracker scriptTracker){
		StringBuffer out = new StringBuffer();
		
		//build library script first
		if(resource.getId().getType().equals(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY)){
			out.append("\n");
			out.append(buildScriptForLibrary(resource, scriptTracker));
			out.append("\n");
		}
		
		if(resource.getInfo().getValue(HAPRuntimeImpJSRhino.ADDTORESOURCEMANAGER).equals(HAPRuntimeImpJSRhino.ADDTORESOURCEMANAGER))  return out.toString();
		
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

		templateParms.put(HAPResourceDataJSValue.VALUE, valueScript);

		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "ResourceScript.temp");
		out.append("\n");
		String resoruceDataScript = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		out.append(resoruceDataScript);
		out.append("\n");
		
		scriptTracker.addScript(resoruceDataScript);
		
		return out.toString();
	}
	
	public static String buildScriptForLibrary(HAPResource resource, ScriptTracker scriptTracker){
		StringBuffer out = new StringBuffer();
		HAPResourceDataJSLibrary resourceLibrary = (HAPResourceDataJSLibrary)resource.getResourceData();
		List<URI> uris = resourceLibrary.getURIs();
		for(URI uri : uris){
			scriptTracker.addFile(new File(uri).toString());
			
			String content = HAPFileUtility.readFile(new File(uri));
			out.append(content);
		}
		return out.toString();
	}
	
	public static String buildScriptForExecuteExpression(HAPExpression expression){
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put("expression", HAPSerializeManager.getInstance().toStringValue(expression, HAPSerializationFormat.JSON));
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPRuntimeJSScriptUtility.class, "ExecuteScript.temp");
		String out = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		return out;
	}
}
