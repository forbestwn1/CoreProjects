package com.nosliw.uiresource.page.tag;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;

public class HAPUITagDefinitionControl extends HAPUITagDefinition{

	public HAPUITagDefinitionControl() {
		super(HAPConstant.UITAG_TYPE_CONTROL);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
