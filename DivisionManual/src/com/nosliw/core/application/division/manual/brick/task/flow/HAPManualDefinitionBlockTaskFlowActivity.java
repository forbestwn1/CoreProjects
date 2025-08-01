package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickWithEntityInfo;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPBlockTaskFlowActivity;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPTaskFlowNext;

abstract public class HAPManualDefinitionBlockTaskFlowActivity extends HAPManualDefinitionBrickWithEntityInfo{

	public HAPManualDefinitionBlockTaskFlowActivity(HAPIdBrickType brickTypeId) {
		super(brickTypeId);
	}

	public HAPTaskFlowNext getNext() {    return (HAPTaskFlowNext)this.getAttributeValueOfValue(HAPBlockTaskFlowActivity.NEXT);      }
	public void setNext(HAPTaskFlowNext next) {    this.setAttributeValueWithValue(HAPBlockTaskFlowActivity.NEXT, next);      }
	
}
