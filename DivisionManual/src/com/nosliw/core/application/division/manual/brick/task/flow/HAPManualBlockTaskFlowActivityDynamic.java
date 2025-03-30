package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.core.application.HAPAddressValue;
import com.nosliw.core.application.HAPValueOfDynamic;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowActivityDynamic;

public class HAPManualBlockTaskFlowActivityDynamic extends HAPManualBlockTaskFlowActivity implements HAPBlockTaskFlowActivityDynamic{

	@Override
	public HAPValueOfDynamic getTask() {    return this.getAttributeValueOfDynamic(TASK);   }
	public void setDefinition(HAPValueOfDynamic dynamicValue) {    this.setAttributeValueWithDynamic(TASK, dynamicValue);      }

	@Override
	public HAPAddressValue getTaskAddress() {   return (HAPAddressValue)this.getAttributeValueOfValue(TASKADDRESS);  }
	public void setRuntime(HAPAddressValue runtime) {    this.setAttributeValueWithValue(TASKADDRESS, runtime);     }
}
