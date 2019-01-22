package com.nosliw.data.core.script.context;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPUtilityContextScript {

	public static HAPScript buildContextInitScript(HAPContextGroup context) {
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		//build init output object 
		JSONObject output = HAPUtilityContext.buildDefaultJsonObject(context);
		templateParms.put("outputInit", HAPJsonUtility.formatJson(output.toString()));

		InputStream templateStream = HAPFileUtility.getInputStreamOnClassPath(HAPUtilityContextScript.class, "ProcessInitFunction.temp");
		String script = HAPStringTemplateUtil.getStringValue(templateStream, templateParms);
		return new HAPScript(script);
	}
	
}
