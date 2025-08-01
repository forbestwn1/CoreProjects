package com.nosliw.core.application.division.manual.brick.task.script.task;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickImp;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.xxx.application1.brick.task.script.task.HAPBlockTaskTaskScript;

public class HAPManualBlockTaskTaskScript extends HAPManualBrickImp implements HAPBlockTaskTaskScript{

	@Override
	public HAPEntityOrReference getTaskInterface() {    return this.getAttributeValueOfBrick(TASKINTERFACE);  }
	public void setTaskInterface(HAPEntityOrReference taskInterface) {   this.setAttributeValueWithBrick(TASKINTERFACE, taskInterface);     }

	@Override
	public HAPResourceId getScriptResourceId() {   return (HAPResourceId)this.getAttributeValueOfValue(SCRIPTRESOURCEID);  }
	public void setScriptResourceId(HAPResourceId resourceId) {    this.setAttributeValueWithValue(SCRIPTRESOURCEID, resourceId);  }

}
