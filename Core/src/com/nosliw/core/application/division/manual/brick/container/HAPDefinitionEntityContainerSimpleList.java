package com.nosliw.core.application.division.manual.brick.container;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.HAPManualEntity;

public class HAPDefinitionEntityContainerSimpleList<T extends HAPManualEntity> extends HAPDefinitionEntityContainer<T>{

	public static String ELEMENT = "element";
	
	
	
	public HAPDefinitionEntityContainerSimpleList(HAPIdBrickType entityTypeId) {
		super(entityTypeId);
	}

	
}
