package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowActivity;
import com.nosliw.core.application.brick.task.flow.HAPTaskFlowNext;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;

abstract public class HAPManualDefinitionBlockTaskFlowActivity extends HAPManualDefinitionBrick{

	public HAPManualDefinitionBlockTaskFlowActivity(HAPIdBrickType brickTypeId) {
		super(brickTypeId);
	}

	public HAPTaskFlowNext getNext() {    return (HAPTaskFlowNext)this.getAttributeValueOfValue(HAPBlockTaskFlowActivity.NEXT);      }
	public void setNext(HAPTaskFlowNext next) {    this.setAttributeValueWithValue(HAPBlockTaskFlowActivity.NEXT, next);      }
	
}
