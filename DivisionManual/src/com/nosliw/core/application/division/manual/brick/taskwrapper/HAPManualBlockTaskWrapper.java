package com.nosliw.core.application.division.manual.brick.taskwrapper;

import com.nosliw.core.application.brick.taskwrapper.HAPBlockTaskWrapper;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;

public class HAPManualBlockTaskWrapper extends HAPManualBrickImp implements HAPBlockTaskWrapper{

	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public String getTaskType() {	return (String)this.getAttributeValueOfValue(TASKTYPE);	}
	public void setTaskType(String taskType) {    this.setAttributeValueWithValue(TASKTYPE, taskType);     }

//	private HAPResultBrick getTaskResult() {
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
//		HAPResultBrick brickResult = this.getTaskResult();
//		if(brickResult.isInternalBrick()) {
//			return ((HAPManualBrick)brickResult.getBrick()).getOtherInternalValuePortContainer();
//		}
//		throw new RuntimeException();
//	}
//	@Override
//	public HAPContainerValuePorts getOtherExternalValuePortContainer() {   
//		HAPResultBrick brickResult = this.getTaskResult();
//		if(brickResult.isInternalBrick()) {
//			return ((HAPManualBrick)brickResult.getBrick()).getOtherExternalValuePortContainer();
//		}
//		throw new RuntimeException();
//	}

}
