package com.nosliw.core.application.service;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.common.brick.HAPBrickImp;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.valueport.HAPContainerValuePorts;

public class HAPBlockInteractiveInterfaceTaskImp extends HAPBrickImp implements HAPBlockInteractiveInterfaceTask{

	public HAPBlockInteractiveInterfaceTaskImp(){
		this.setBrickType(HAPEnumBrickType.INTERACTIVETASKINTERFACE_100);
	}
	
	@Override
	public HAPContainerValuePorts getInternalValuePorts() {  		return null;	}

	@Override
	public HAPContainerValuePorts getExternalValuePorts() {		return null;	}

	@Override
	public HAPInteractiveTask getValue() {    return (HAPInteractiveTask)this.getAttributeValueOfValue(VALUE);  }
	public void setValue(HAPInteractiveTask value) {   this.setAttributeValueWithValue(VALUE, value);      }

}
