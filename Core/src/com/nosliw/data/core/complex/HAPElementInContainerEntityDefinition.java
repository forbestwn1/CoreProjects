package com.nosliw.data.core.complex;

import com.nosliw.common.info.HAPEntityInfo;

public interface HAPElementInContainerEntityDefinition extends HAPEntityInfo{

	public static final String TYPE_REFERENCE = "reference";
	
	public static final String TYPE_ENTITY = "entity";
	
	String getElementType();

	HAPElementInContainerEntityDefinition cloneDefinitionEntityElementInContainer();
	
}
