package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.division.manual.brick.container.HAPManualDefinitionBrickContainer;
import com.nosliw.core.application.division.manual.common.task.HAPManualDefinitionWithTaskInterfaceInteractive;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPBlockTaskFlowFlow;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPTaskFlowNext;

public class HAPManualDefinitionBlockTaskFlowFlow extends HAPManualDefinitionBrick implements HAPManualDefinitionWithTaskInterfaceInteractive{

	public HAPManualDefinitionBlockTaskFlowFlow() {
		super(HAPEnumBrickType.TASK_TASK_FLOW_100);
	}

	@Override
	protected void init() {
		this.setAttributeValueWithBrick(HAPBlockTaskFlowFlow.ACTIVITY, this.getManualBrickManager().newBrickDefinition(HAPEnumBrickType.CONTAINER_100));
	}

	@Override
	public HAPEntityOrReference getTaskInterface() {    return this.getAttributeValueOfBrick(HAPManualDefinitionWithTaskInterfaceInteractive.TASKINTERFACE);  }
	@Override
	public void setTaskInterface(HAPEntityOrReference taskInterface) {    this.setAttributeValueWithBrick(HAPManualDefinitionWithTaskInterfaceInteractive.TASKINTERFACE, taskInterface);       }
	
	public HAPTaskFlowNext getStart() {	return (HAPTaskFlowNext)this.getAttributeValueOfValue(HAPBlockTaskFlowFlow.START);	}
	public void setStart(HAPTaskFlowNext start) {  this.setAttributeValueWithValue(HAPBlockTaskFlowFlow.START, start);  }
	
	public HAPManualDefinitionBrickContainer getActivityContainer() {		return (HAPManualDefinitionBrickContainer)this.getAttributeValueOfBrick(HAPBlockTaskFlowFlow.ACTIVITY);	}
	
	public void addActivity(HAPManualDefinitionBlockTaskFlowActivity activity) {
		this.getActivityContainer().addElementWithBrick(activity);
	}
	
}
