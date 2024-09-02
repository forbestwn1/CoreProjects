package com.nosliw.core.application.division.manual.brick.taskwrapper;

import com.nosliw.core.application.brick.taskwrapper.HAPBlockTaskWrapper;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;

public class HAPManualBlockSimpleTaskWrapper extends HAPManualBrickImp implements HAPBlockTaskWrapper{

	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public String getTaskType() {	return (String)this.getAttributeValueOfValue(TASKTYPE);	}
	public void setTaskType(String taskType) {    this.setAttributeValueWithValue(TASKTYPE, taskType);     }

	public HAPManualBrick getTask() {   return (HAPManualBrick)this.getAttributeValueOfBrick(HAPBlockTaskWrapper.TASK);      }
	
	@Override
	public HAPGroupValuePorts addOtherInternalValuePortGroup(HAPGroupValuePorts valuePortGroup) {	return this.getTask().addOtherInternalValuePortGroup(valuePortGroup);	}
	@Override
	public HAPGroupValuePorts addOtherExternalValuePortGroup(HAPGroupValuePorts valuePortGroup) {     return this.getTask().addOtherExternalValuePortGroup(valuePortGroup);	}

}
