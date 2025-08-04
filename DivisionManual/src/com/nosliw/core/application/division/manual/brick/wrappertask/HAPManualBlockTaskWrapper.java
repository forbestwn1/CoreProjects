package com.nosliw.core.application.division.manual.brick.wrappertask;

import com.nosliw.core.application.division.manual.HAPManualBrickImp;
import com.nosliw.core.xxx.application1.brick.wrappertask.HAPBlockTaskWrapper;

public class HAPManualBlockTaskWrapper extends HAPManualBrickImp implements HAPBlockTaskWrapper{

	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public String getTaskType() {	return (String)this.getAttributeValueOfValue(TASKTYPE);	}
	public void setTaskType(String taskType) {    this.setAttributeValueWithValue(TASKTYPE, taskType);     }

//	private HAPResultBrickDescentValue getTaskResult() {
//		return HAPUtilityBrick.getAttributeResultOfBrickGlobal(this, HAPBlockTaskWrapper.TASK, getBrickManager());
//	}
//	
//	public boolean isLocalTask() {     return this.getTaskResult().isInternalBrick();     }
//	
//	@Override
//	public HAPContainerValuePorts getInternalValuePorts(){  return this.getTaskResult().getBrick().getInternalValuePorts();	  }
//
//	@Override
//	public HAPContainerValuePorts getExternalValuePorts(){  return this.getTaskResult().getBrick().getExternalValuePorts(); 	}
//
//	@Override
//	public HAPContainerValuePorts getOtherInternalValuePortContainer() {
//		HAPResultBrickDescentValue brickResult = this.getTaskResult();
//		if(brickResult.isInternalBrick()) {
//			return ((HAPManualBrick)brickResult.getBrick()).getOtherInternalValuePortContainer();
//		}
//		throw new RuntimeException();
//	}
//	@Override
//	public HAPContainerValuePorts getOtherExternalValuePortContainer() {   
//		HAPResultBrickDescentValue brickResult = this.getTaskResult();
//		if(brickResult.isInternalBrick()) {
//			return ((HAPManualBrick)brickResult.getBrick()).getOtherExternalValuePortContainer();
//		}
//		throw new RuntimeException();
//	}

}
