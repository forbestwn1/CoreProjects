package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowFlow;
import com.nosliw.core.application.brick.task.flow.HAPTaskFlowNext;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;
import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainer;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockTaskFlowFlow extends HAPManualDefinitionBrick implements HAPWithBlockInteractiveTask{

	public HAPManualDefinitionBlockTaskFlowFlow() {
		super(HAPEnumBrickType.TASK_TASK_FLOW_100);
	}

	@Override
	protected void init() {
		this.setAttributeValueWithBrick(HAPBlockTaskFlowFlow.ACTIVITY, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100));
	}
 
	@Override
	public HAPEntityOrReference getTaskInterface() {    return this.getAttributeValueOfBrick(HAPWithBlockInteractiveTask.TASKINTERFACE);  }
	@Override
	public void setTaskInterface(HAPEntityOrReference taskInterface) {    this.setAttributeValueWithBrick(HAPWithBlockInteractiveTask.TASKINTERFACE, taskInterface);       }
	
	public HAPTaskFlowNext getStart() {	return (HAPTaskFlowNext)this.getAttributeValueOfValue(HAPBlockTaskFlowFlow.START);	}
	public void setStart(HAPTaskFlowNext start) {  this.setAttributeValueWithValue(HAPBlockTaskFlowFlow.START, start);  }
	
	public HAPManualDefinitionBrickContainer getActivityContainer() {		return (HAPManualDefinitionBrickContainer)this.getAttributeValueOfBrick(HAPBlockTaskFlowFlow.ACTIVITY);	}
	
	public void addActivity(HAPManualDefinitionBlockTaskFlowActivity activity) {
		this.getActivityContainer().addElementWithBrick(activity);
	}
	
}
