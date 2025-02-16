package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowActivity;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowActivityTask;
import com.nosliw.core.application.brick.task.flow.HAPTaskFlowNext;

public class HAPManualDefinitionBlockTaskFlowActivityTask extends HAPManualDefinitionBlockTaskFlowActivity{

	public HAPManualDefinitionBlockTaskFlowActivityTask() {
		super(HAPEnumBrickType.TASK_TASK_ACTIVITYTASK_100);
	}

	@Override
	public HAPTaskFlowNext getNext() {   return (HAPTaskFlowNext)this.getAttributeValueOfValue(HAPBlockTaskFlowActivity.NEXT);  }
	@Override
	public void setNext(HAPTaskFlowNext next) {   this.setAttributeValueWithValue(HAPBlockTaskFlowActivity.NEXT, next);     }

	public HAPEntityOrReference getTask() {		return this.getAttributeValueOfBrick(HAPBlockTaskFlowActivityTask.TASK);	}
	public void setTask(HAPEntityOrReference task) {   this.setAttributeValueWithBrick(HAPBlockTaskFlowActivityTask.TASK, task);     }
	
}
