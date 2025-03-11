package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPValueOfDynamic;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowActivityDynamic;
import com.nosliw.core.application.brick.task.flow.HAPTaskFlowAddressTask;

public class HAPManualDefinitionBlockTaskFlowActivityDynamic extends HAPManualDefinitionBlockTaskFlowActivity{

	public HAPManualDefinitionBlockTaskFlowActivityDynamic() {
		super(HAPEnumBrickType.TASK_TASK_ACTIVITYDYNAMIC_100);
	}

	public HAPValueOfDynamic getDefinition() {   return this.getAttributeValueOfDynamic(HAPBlockTaskFlowActivityDynamic.DEFINITION);    }
	public void setDefinition(HAPValueOfDynamic definition) {    this.setAttributeValueWithDynamic(HAPBlockTaskFlowActivityDynamic.DEFINITION, definition);    }
	
	public HAPTaskFlowAddressTask getRuntime() {    return (HAPTaskFlowAddressTask)this.getAttributeValueOfValue(HAPBlockTaskFlowActivityDynamic.RUNTIME);       }
	public void setRuntime(HAPTaskFlowAddressTask runtime) {    this.setAttributeValueWithValue(HAPBlockTaskFlowActivityDynamic.RUNTIME, runtime);      }
	
}
