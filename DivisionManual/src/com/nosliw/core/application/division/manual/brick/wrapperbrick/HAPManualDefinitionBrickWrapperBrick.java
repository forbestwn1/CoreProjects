package com.nosliw.core.application.division.manual.brick.wrapperbrick;

import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrickWithEntityInfo;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;

public class HAPManualDefinitionBrickWrapperBrick extends HAPManualDefinitionBrickWithEntityInfo{

	public static final String BRICKTYPE = "brickType";
	
	public HAPManualDefinitionBrickWrapperBrick() {
		super(HAPEnumBrickType.WRAPPERBRICK_100);
	}

}
