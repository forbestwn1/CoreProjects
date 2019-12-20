package com.nosliw.data.core.resource.external;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPExternalMappingUtility {

	public static final String ATTRIBUTE_OVERRIDE_MODE = "overide"; 
	public static final String OVERRIDE_MODE_CONFIGURABLE = "configurable"; 
	public static final String OVERRIDE_MODE_NONE = "none"; 

	public static final String ATTRIBUTE_FLAG_OVERRIDE = "flagOveride"; 

	
	public static void parseDefinition(JSONObject externalDefJson, HAPDefinitionExternalMapping externalMapping) {
		for(Object key : externalDefJson.keySet()) {
			String type = (String)key;
			JSONArray byNameArray = externalDefJson.getJSONArray(type);
			for(int i=0; i<byNameArray.length(); i++) {
				HAPDefinitionExternalMappingEle ele = new HAPDefinitionExternalMappingEle(type);
				ele.buildObject(byNameArray.getJSONObject(i), HAPSerializationFormat.JSON);
				externalMapping.addElement(type, ele);
			}
		}
	}

	public static void setOverridenByParent(HAPDefinitionExternalMappingEle ele) {
		ele.getInfo().setValue(ATTRIBUTE_FLAG_OVERRIDE, Boolean.TRUE);
	}
	
	public static boolean isOverridenByParent(HAPDefinitionExternalMappingEle ele) {
		return ele.getInfoValue(ATTRIBUTE_FLAG_OVERRIDE).equals(Boolean.TRUE);
	}
	
	public static boolean isOverridenByParentMode(HAPDefinitionExternalMappingEle ele) {
		String mode = (String)ele.getInfoValue(ATTRIBUTE_OVERRIDE_MODE);
		if(mode==null)   mode =  OVERRIDE_MODE_NONE;
		return OVERRIDE_MODE_CONFIGURABLE.equals(mode);
	}
	
}
