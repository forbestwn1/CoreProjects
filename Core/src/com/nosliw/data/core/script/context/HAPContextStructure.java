package com.nosliw.data.core.script.context;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPContextStructure {

	@HAPAttribute
	public static final String TYPE = "type";

	String getType();
	
	boolean isFlat();

	HAPContextDefinitionRoot getElement(String eleName);
	
	HAPContextStructure cloneContextStructure();
	
}
