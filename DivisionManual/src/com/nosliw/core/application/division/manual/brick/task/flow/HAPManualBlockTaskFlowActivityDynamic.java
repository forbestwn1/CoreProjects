package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.core.application.HAPValueOfDynamic;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowActivityDynamic;
import com.nosliw.core.application.brick.task.flow.HAPTaskFlowAddressTask;

public class HAPManualBlockTaskFlowActivityDynamic extends HAPManualBlockTaskFlowActivity implements HAPBlockTaskFlowActivityDynamic{

	@Override
	public HAPValueOfDynamic getDefinition() {    return this.getAttributeValueOfDynamic(DEFINITION);   }
	public void setDefinition(HAPValueOfDynamic dynamicValue) {    this.setAttributeValueWithDynamic(DEFINITION, dynamicValue);      }

	@Override
	public HAPTaskFlowAddressTask getRuntime() {   return (HAPTaskFlowAddressTask)this.getAttributeValueOfValue(RUNTIME);  }
	public void setRuntime(HAPTaskFlowAddressTask runtime) {    this.setAttributeValueWithValue(RUNTIME, runtime);     }
}
