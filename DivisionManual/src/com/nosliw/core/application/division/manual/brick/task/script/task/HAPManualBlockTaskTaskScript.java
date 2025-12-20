package com.nosliw.core.application.division.manual.brick.task.script.task;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.brick.task.script.task.HAPBlockTaskTaskScript;
import com.nosliw.core.application.division.manual.core.HAPManualBrickImp;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualBlockTaskTaskScript extends HAPManualBrickImp implements HAPBlockTaskTaskScript{

	@Override
	public void init() {
		super.init();
	}
	
	@Override
	public HAPEntityOrReference getTaskInterface() {    return this.getAttributeValueOfBrick(TASKINTERFACE);  }
	@Override
	public void setTaskInterface(HAPEntityOrReference taskInterface) {   this.setAttributeValueWithBrick(TASKINTERFACE, taskInterface);     }

	@Override
	public HAPResourceId getScriptResourceId() {   return (HAPResourceId)this.getAttributeValueOfValue(SCRIPTRESOURCEID);  }
	public void setScriptResourceId(HAPResourceId resourceId) {    this.setAttributeValueWithValue(SCRIPTRESOURCEID, resourceId);  }
	
	@Override
	public Object getExtra() {   return this.getAttributeValueOfValue(EXTRA);  }
	public void setExtra(Object extra) {    this.setAttributeValueWithValue(EXTRA, extra);  }
}
 