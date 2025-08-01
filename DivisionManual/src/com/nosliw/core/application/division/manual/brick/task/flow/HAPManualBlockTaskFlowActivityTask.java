package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.core.xxx.application1.brick.task.flow.HAPBlockTaskFlowActivity;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPBlockTaskFlowActivityTask;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPTaskFlowNext;

public class HAPManualBlockTaskFlowActivityTask extends HAPManualBlockTaskFlowActivity implements HAPBlockTaskFlowActivity{

	@Override
	public HAPTaskFlowNext getNext() {   return (HAPTaskFlowNext)this.getAttributeValueOfValue(HAPBlockTaskFlowActivityTask.NEXT);   }
	@Override
	public void setNext(HAPTaskFlowNext next) {   this.setAttributeValueWithValue(HAPBlockTaskFlowActivityTask.NEXT, next);    }

}
