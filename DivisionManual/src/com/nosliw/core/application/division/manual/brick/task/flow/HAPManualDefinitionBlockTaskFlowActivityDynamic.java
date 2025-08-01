package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.core.application.HAPValueOfDynamic;
import com.nosliw.core.xxx.application1.HAPAddressValue;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPBlockTaskFlowActivityDynamic;

public class HAPManualDefinitionBlockTaskFlowActivityDynamic extends HAPManualDefinitionBlockTaskFlowActivity{

	public HAPManualDefinitionBlockTaskFlowActivityDynamic() {
		super(HAPEnumBrickType.TASK_TASK_ACTIVITYDYNAMIC_100);
	}

	public HAPValueOfDynamic getTask() {   return this.getAttributeValueOfDynamic(HAPBlockTaskFlowActivityDynamic.TASK);    }
	public void setTask(HAPValueOfDynamic task) {    this.setAttributeValueWithDynamic(HAPBlockTaskFlowActivityDynamic.TASK, task);    }
	
	public HAPAddressValue getTaskAddress() {    return (HAPAddressValue)this.getAttributeValueOfValue(HAPBlockTaskFlowActivityDynamic.TASKADDRESS);       }
	public void setTaskAddress(HAPAddressValue taskAddress) {    this.setAttributeValueWithValue(HAPBlockTaskFlowActivityDynamic.TASKADDRESS, taskAddress);      }
	
}
