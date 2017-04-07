package com.nosliw.data.core.runtime.js;

import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPConstantManager;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPRuntimeJSUtility {

	public static String buildScriptForResource(HAPResource resource){
		
		String valueScript = null;
		if(resource.getResourceData() instanceof HAPResourceDataJSValue){
			valueScript = ((HAPResourceDataJSValue)resource.getResourceData()).getValue();
		}
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put(HAPResourceId.ID, resource.getId().getId());
		templateParms.put(HAPResourceId.TYPE, resource.getId().getType());
		templateParms.put(HAPResource.INFO, HAPSerializeManager.getInstance().toStringValue(resource.getInfo(), HAPSerializationFormat.JSON));
		templateParms.put(HAPResourceDataJSValue.VALUE, valueScript);
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPConstantManager.class, "ResourceScript.temp");
		String out = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);
		
		if(resource.getId().getType().equals(HAPConstant.DATAOPERATION_RESOURCE_TYPE_LIBRARY)){
			out = out + "\n" + buildScriptForLibrary(resource);
		}
		
		return out;
	}
	
	public static String buildScriptForLibrary(HAPResource resource){
		StringBuffer out = new StringBuffer();
		HAPResourceDataLibrary resourceLibrary = (HAPResourceDataLibrary)resource.getResourceData();
		List<URI> uris = resourceLibrary.getURIs();
		for(URI uri : uris){
			String content = HAPFileUtility.readFile(new File(uri));
			out.append(content);
		}
		return out.toString();
	}
}
