package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowActivityTask;
import com.nosliw.core.application.brick.task.flow.HAPTaskFlowNext;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockTaskFlowActivityTask extends HAPManualDefinitionBrick implements HAPManualDefinitionFlowActivityTask{

	public HAPManualDefinitionBlockTaskFlowActivityTask() {
		super(HAPEnumBrickType.TASK_TASK_ACTIVITYTASK_100);
	}

	@Override
	public HAPTaskFlowNext getNext() {   return (HAPTaskFlowNext)this.getAttributeValueOfValue(NEXT);  }
	public void setNext(HAPTaskFlowNext next) {   this.setAttributeValueWithValue(NEXT, next);     }

	public HAPEntityOrReference getTask() {		return this.getAttributeValueOfBrick(HAPBlockTaskFlowActivityTask.TASK);	}
	public void setTask(HAPEntityOrReference task) {   this.setAttributeValueWithBrick(HAPBlockTaskFlowActivityTask.TASK, task);     }
	
}
