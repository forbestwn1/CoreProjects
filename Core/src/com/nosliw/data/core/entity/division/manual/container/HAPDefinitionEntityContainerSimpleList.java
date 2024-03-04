package com.nosliw.data.core.entity.division.manual.container;

import com.nosliw.data.core.entity.HAPIdEntityType;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;

public class HAPDefinitionEntityContainerSimpleList<T extends HAPManualEntity> extends HAPDefinitionEntityContainer<T>{

	public static String ELEMENT = "element";
	
	
	
	public HAPDefinitionEntityContainerSimpleList(HAPIdEntityType entityTypeId) {
		super(entityTypeId);
	}

	
}
