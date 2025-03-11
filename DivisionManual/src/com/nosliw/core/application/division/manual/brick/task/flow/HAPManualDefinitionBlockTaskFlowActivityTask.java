package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowActivityTask;

public class HAPManualDefinitionBlockTaskFlowActivityTask extends HAPManualDefinitionBlockTaskFlowActivity{

	public HAPManualDefinitionBlockTaskFlowActivityTask() {
		super(HAPEnumBrickType.TASK_TASK_ACTIVITYTASK_100);
	}

	public HAPEntityOrReference getTask() {		return this.getAttributeValueOfBrick(HAPBlockTaskFlowActivityTask.TASK);	}
	public void setTask(HAPEntityOrReference task) {   this.setAttributeValueWithBrick(HAPBlockTaskFlowActivityTask.TASK, task);     }
	
}
