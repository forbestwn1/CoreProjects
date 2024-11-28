package com.nosliw.core.application.division.manual.brick.wrapperbrick;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickWithEntityInfo;

public class HAPManualDefinitionBrickWrapperBrick extends HAPManualDefinitionBrickWithEntityInfo{

	public static final String BRICKTYPE = "brickType";
	
	public HAPManualDefinitionBrickWrapperBrick() {
		super(HAPEnumBrickType.WRAPPERBRICK_100);
	}

}
