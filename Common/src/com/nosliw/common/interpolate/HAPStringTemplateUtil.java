package com.nosliw.common.interpolate;

import java.io.InputStream;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPUtilityFile;

public class HAPStringTemplateUtil {

	public static String getTemplateFromFile(InputStream file){
		return HAPUtilityFile.readFile(file);
	}
	
	public static String getStringValue(InputStream templateStream, Map<String, String> parms){
		String template = HAPUtilityFile.readFile(templateStream);
		return getStringValue(template, parms);
	}
	
	public static String getStringValue(String template, Map<String, String> parms){
		if(HAPBasicUtility.isStringNotEmpty(template)){
			for(String key : parms.keySet()){
				String fill = parms.get(key);
				if(fill==null)  fill = "";
				template = template.replace("||"+key+"||", fill);
			}
		}
		return template;
	}
}
