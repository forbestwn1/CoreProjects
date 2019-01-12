package com.nosliw.uiresource.page.execute;

import java.util.Map;

import com.nosliw.uiresource.page.definition.HAPDefinitionUIUnit;

public class HAPExecutableUIUnitResource extends HAPExecutableUIUnit{

	public HAPExecutableUIUnitResource(HAPDefinitionUIUnit uiUnitDefinition, String id) {
		super(uiUnitDefinition, id);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
}
