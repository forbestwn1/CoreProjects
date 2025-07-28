package com.nosliw.data.core.valuestructure1;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.interpolate.HAPStringTemplateUtil;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.common.structure.HAPRootStructure;

public class HAPUtilityValueStructureScript {

	public static HAPJsonTypeScript buildValueStructureInitScript(HAPExecutableValueStructure valueStructure) {
		Map<String, String> templateParms = new LinkedHashMap<String, String>();
		//build init output object 
		JSONObject output = buildDefaultJsonObject(valueStructure);
		templateParms.put("outputInit", HAPUtilityJson.formatJson(output.toString()));

		InputStream templateStream = HAPUtilityFile.getInputStreamOnClassPath(HAPUtilityValueStructureScript.class, "ValueStructureInitFunction.temp");
		String script = HAPStringTemplateUtil.getStringValue(templateStream, templateParms);
		return new HAPJsonTypeScript(script);
	}
	
	//build default value structure for executable value structure
	public static JSONObject buildDefaultJsonObject(HAPExecutableValueStructure valueStructure) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		for(HAPRootStructure root : valueStructure.getAllRoots()) {
			Object value = root.getDefaultValue();
			if(value!=null) {
				jsonMap.put(root.getLocalId(), value.toString());
			}
		}
		return new JSONObject(HAPUtilityJson.buildMapJson(jsonMap));
	}

}
