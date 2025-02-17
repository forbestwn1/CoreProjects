package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.brick.task.flow.HAPBlockTaskFlowActivityTask;
import com.nosliw.core.application.brick.task.flow.HAPTaskFlowNext;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickWithEntityInfo;

public class HAPManualBlockTaskFlowActivityTask extends HAPManualBrickWithEntityInfo implements HAPBlockTaskFlowActivityTask{

	@Override
	public HAPTaskFlowNext getNext() {   return (HAPTaskFlowNext)this.getAttributeValueOfValue(HAPBlockTaskFlowActivityTask.NEXT);   }
	public void setNext(HAPTaskFlowNext next) {   this.setAttributeValueWithValue(HAPBlockTaskFlowActivityTask.NAME, next);    }

	@Override
	public HAPEntityOrReference getTask() {   return this.getAttributeValueOfBrick(HAPBlockTaskFlowActivityTask.TASK);  }

}
