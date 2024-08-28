package com.nosliw.core.application.division.manual.brick.taskwrapper;

import com.nosliw.core.application.brick.taskwrapper.HAPBlockTaskWrapper;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;

public class HAPManualBlockSimpleTaskWrapper extends HAPManualBrickImp implements HAPBlockTaskWrapper{

	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public String getTaskType() {	return (String)this.getAttributeValueOfValue(TASKTYPE);	}
	public void setTaskType(String taskType) {    this.setAttributeValueWithValue(TASKTYPE, taskType);     }
}
