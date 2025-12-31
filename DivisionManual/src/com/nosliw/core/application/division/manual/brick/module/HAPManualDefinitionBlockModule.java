package com.nosliw.core.application.division.manual.brick.module;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.module.HAPBlockModule;
import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainer;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockModule extends HAPManualDefinitionBrick{

	public static final String BRICKTYPE = "brickType";

	public HAPManualDefinitionBlockModule() {
		super(HAPEnumBrickType.MODULE_100);
	}

	@Override
	protected void init() {
		this.setAttributeValueWithBrick(HAPBlockModule.TASK, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100));
		this.setAttributeValueWithBrick(HAPBlockModule.COMMAND, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100));
		this.setAttributeValueWithBrick(HAPBlockModule.PAGE, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100));
	}

	public HAPManualDefinitionBrickContainer getTasks() {   return (HAPManualDefinitionBrickContainer)this.getAttributeValueOfBrick(HAPBlockModule.TASK);   }
	public void addTask(HAPEntityOrReference task) {    this.getTasks().addElementWithBrickOrReference(task);    }
	
	public HAPManualDefinitionBrickContainer getCommands() {   return (HAPManualDefinitionBrickContainer)this.getAttributeValueOfBrick(HAPBlockModule.COMMAND);   }
	public void addCommand(HAPEntityOrReference command) {    this.getCommands().addElementWithBrickOrReference(command);    }

	public HAPManualDefinitionBrickContainer getPages() {   return (HAPManualDefinitionBrickContainer)this.getAttributeValueOfBrick(HAPBlockModule.PAGE);   }
	public void addPage(HAPEntityOrReference page) {    this.getPages().addElementWithBrickOrReference(page);    }

}
