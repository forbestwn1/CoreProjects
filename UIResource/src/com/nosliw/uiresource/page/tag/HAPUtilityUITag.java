package com.nosliw.uiresource.page.tag;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPUtilityUITag {

	public static Map<String, String> getTagAttributeValue(HAPUITagDefinition tagDef, Map<String, String> attributes){
		Map<String, String> out = new LinkedHashMap<String, String>();
		for(HAPUITagDefinitionAttribute attrDef : tagDef.getAllAttributeDef()) {
			String defaultValue = attrDef.getInitValue();
			if(defaultValue!=null) {
				out.put(attrDef.getName(), defaultValue);
			}
		}
		if(attributes!=null) {
			out.putAll(attributes);
		}
		return out;
	}
	
	
}
