package com.nosliw.core.application.division.manual.brick.interactive.interfacee.task;

import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;

public class HAPManualDefinitionBlockInteractiveInterfaceTask extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockInteractiveInterfaceTask() {
		super(HAPEnumBrickType.INTERACTIVETASKINTERFACE_100);
	}

	public HAPInteractiveTask getValue() {   return (HAPInteractiveTask)this.getAttributeValueOfValue(HAPBlockInteractiveInterfaceTask.VALUE);  }
	public void setValue(HAPInteractiveTask taskInteractive) {      this.setAttributeValueWithValue(HAPManualBlockInteractiveInterfaceTask.VALUE, taskInteractive);       }
	
}
