package com.nosliw.data.core.runtime.js;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPConstantManager;
import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPRuntimeJSUtility {

	public static String buildScriptForResource(HAPResource resource){
		
		String script = null;
		if(resource.getResourceData() instanceof HAPResourceDataScript){
			script = ((HAPResourceDataScript)resource.getResourceData()).getScript();
		}
		
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		templateParms.put(HAPResourceId.ID, resource.getId().getId());
		templateParms.put(HAPResourceId.TYPE, resource.getId().getType());
		templateParms.put(HAPResource.INFO, HAPSerializeManager.getInstance().toStringValue(resource.getInfo(), HAPSerializationFormat.JSON));
		templateParms.put(HAPResourceDataScript.SCRIPT, script);
		
		InputStream javaTemplateStream = HAPFileUtility.getInputStreamOnClassPath(HAPConstantManager.class, "ResourceScript.temp");
		String out = HAPStringTemplateUtil.getStringValue(javaTemplateStream, templateParms);

		return out;
	}
	
	
}
