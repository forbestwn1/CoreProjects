package com.nosliw.core.application.division.manual.brick.task.flow;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.division.manual.HAPManualBrickImp;
import com.nosliw.core.xxx.application1.brick.container.HAPBrickContainer;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPBlockTaskFlowFlow;
import com.nosliw.core.xxx.application1.brick.task.flow.HAPTaskFlowNext;

public class HAPManualBlockTaskFlowFlow extends HAPManualBrickImp implements HAPBlockTaskFlowFlow{

	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public HAPBrickContainer getActivities() {   return (HAPBrickContainer)this.getAttributeValueOfBrickLocal(HAPBlockTaskFlowFlow.ACTIVITY);   }

	@Override
	public HAPTaskFlowNext getStart() {   return (HAPTaskFlowNext)this.getAttributeValueOfValue(HAPBlockTaskFlowFlow.START);  }
	public void setStart(HAPTaskFlowNext start) {   this.setAttributeValueWithValue(HAPBlockTaskFlowFlow.START, start);    }
	
	@Override
	public HAPEntityOrReference getTaskInterface() {    return this.getAttributeValueOfBrick(TASKINTERFACE);  }
	public void setTaskInterface(HAPEntityOrReference taskInterface) {   this.setAttributeValueWithBrick(TASKINTERFACE, taskInterface);     }

}
