package com.nosliw.core.application.division.manual.brick.container;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;

public class HAPManualBrickContainerSimpleList<T extends HAPManualBrick> extends HAPManualBrickContainer<T>{

	public static String ELEMENT = "element";
	
	
	
	public HAPManualBrickContainerSimpleList(HAPIdBrickType entityTypeId) {
		super(entityTypeId);
	}

	
}
