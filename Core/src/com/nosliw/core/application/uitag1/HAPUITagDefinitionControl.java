package com.nosliw.core.application.uitag1;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPUITagDefinitionControl extends HAPUITagDefinition{

	public HAPUITagDefinitionControl() {
		super(HAPConstantShared.UITAG_TYPE_CONTROL);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
