package com.nosliw.core.application.division.manual.brick.module;

import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainer;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.module.HAPBlockModule;

public class HAPManualDefinitionBlockModule extends HAPManualDefinitionBrick{

	public static final String BRICKTYPE = "brickType";

	public HAPManualDefinitionBlockModule() {
		super(HAPEnumBrickType.MODULE_100);
	}

	@Override
	protected void init() {
		this.setAttributeValueWithBrick(HAPBlockModule.BRICK, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100));
		this.setAttributeValueWithBrick(HAPBlockModule.COMMAND, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100));
		this.setAttributeValueWithBrick(HAPBlockModule.LIFECYCLE, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100));
	}

	public HAPManualDefinitionBrickContainer getBricks() {	return (HAPManualDefinitionBrickContainer)this.getAttributeValueOfBrick(HAPBlockModule.BRICK);	}
	public HAPManualDefinitionBrickContainer getLifecycles() {	return (HAPManualDefinitionBrickContainer)this.getAttributeValueOfBrick(HAPBlockModule.LIFECYCLE);	}
	public HAPManualDefinitionBrickContainer getCommand() {	return (HAPManualDefinitionBrickContainer)this.getAttributeValueOfBrick(HAPBlockModule.COMMAND);	}
	
}
