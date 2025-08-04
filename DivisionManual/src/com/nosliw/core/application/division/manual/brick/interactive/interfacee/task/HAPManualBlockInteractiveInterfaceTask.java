package com.nosliw.core.application.division.manual.brick.interactive.interfacee.task;

import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.division.manual.HAPManualBrickImp;
import com.nosliw.core.xxx.application1.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;

public class HAPManualBlockInteractiveInterfaceTask extends HAPManualBrickImp implements HAPBlockInteractiveInterfaceTask{

	@Override
	public HAPInteractiveTask getValue() {   return (HAPInteractiveTask)this.getAttributeValueOfValue(HAPBlockInteractiveInterfaceTask.VALUE);  }

	public void setValue(HAPInteractiveTask taskInteractive) {      this.setAttributeValueWithValue(HAPManualBlockInteractiveInterfaceTask.VALUE, taskInteractive);       }

}
