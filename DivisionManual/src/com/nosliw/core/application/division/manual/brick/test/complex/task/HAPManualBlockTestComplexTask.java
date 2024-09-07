package com.nosliw.core.application.division.manual.brick.test.complex.task;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.brick.test.complex.task.HAPBlockTestComplexTask;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;

public class HAPManualBlockTestComplexTask extends HAPManualBrickImp implements HAPBlockTestComplexTask{

	public HAPManualBlockTestComplexTask() {
	}

	@Override
	public void init() {
		super.init();
		this.setAttributeValueWithValue(VARIABLE, new LinkedHashMap<String, HAPResultReferenceResolve>());
	}

	@Override
	public Map<String, Object> getParms() {		return (Map<String, Object>)this.getAttributeValueOfValue(PARM);	}
	public void setParms(Map<String, Object> parms) {	this.setAttributeValueWithValue(PARM, parms);	}

	@Override
	public Map<String, HAPResultReferenceResolve> getVariables() {    return (Map<String, HAPResultReferenceResolve>)this.getAttributeValueOfValue(VARIABLE);  }

	@Override
	public HAPBlockInteractiveInterfaceTask getTaskInteractive() {   return (HAPBlockInteractiveInterfaceTask)this.getAttributeValueOfBrick(INTERACTIVETASK);  }
	
}
 