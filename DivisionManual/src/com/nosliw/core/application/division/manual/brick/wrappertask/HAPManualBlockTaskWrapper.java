package com.nosliw.core.application.division.manual.brick.wrappertask;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;

public class HAPManualBlockTaskWrapper extends HAPManualBrickImp implements HAPBlockTaskWrapper{

	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public String getTaskType() {	return (String)this.getAttributeValueOfValue(TASKTYPE);	}
	public void setTaskType(String taskType) {    this.setAttributeValueWithValue(TASKTYPE, taskType);     }

	@Override
	public HAPEntityOrReference getTask() {     return this.getAttributeValueOfBrick(HAPBlockTaskWrapper.TASK);        }
	public void setTask(HAPEntityOrReference entityOrRef) {      this.setAttributeValueWithBrick(HAPBlockTaskWrapper.TASK, entityOrRef);        }

}
