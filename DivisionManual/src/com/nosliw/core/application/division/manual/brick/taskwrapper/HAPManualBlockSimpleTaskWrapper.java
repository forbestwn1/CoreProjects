package com.nosliw.core.application.division.manual.brick.taskwrapper;

import com.nosliw.core.application.brick.taskwrapper.HAPBlockTaskWrapper;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;

public class HAPManualBlockSimpleTaskWrapper extends HAPManualBrickBlockSimple implements HAPBlockTaskWrapper{

	
	@Override
	public String getTaskType() {	return (String)this.getAttributeValueOfValue(TASKTYPE);	}
	public void setTaskType(String taskType) {    this.setAttributeValueWithValue(TASKTYPE, taskType);     }
}
