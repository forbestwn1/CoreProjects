package com.nosliw.core.application.brick.interactive.interfacee.task;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.brick.HAPBrickImp;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;

public class HAPBlockInteractiveInterfaceTaskImp extends HAPBrickImp implements HAPBlockInteractiveInterfaceTask{

	public HAPBlockInteractiveInterfaceTaskImp(){
		this.setBrickType(HAPEnumBrickType.INTERACTIVETASKINTERFACE_100);
	}
	
	@Override
	public HAPInteractiveTask getValue() {    return (HAPInteractiveTask)this.getAttributeValueOfValue(VALUE);  }
	public void setValue(HAPInteractiveTask value) {   this.setAttributeValueWithValue(VALUE, value);      }

}
