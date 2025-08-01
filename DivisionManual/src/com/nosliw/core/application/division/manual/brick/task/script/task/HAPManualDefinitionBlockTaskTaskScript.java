package com.nosliw.core.application.division.manual.brick.task.script.task;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.division.manual.common.task.HAPManualDefinitionWithTaskInterfaceInteractive;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.task.script.task.HAPBlockTaskTaskScript;

public class HAPManualDefinitionBlockTaskTaskScript extends HAPManualDefinitionBrick implements HAPManualDefinitionWithTaskInterfaceInteractive{

	public HAPManualDefinitionBlockTaskTaskScript() {
		super(HAPEnumBrickType.TASK_TASK_SCRIPT_100);
	}

	@Override
	public HAPEntityOrReference getTaskInterface() {    return this.getAttributeValueOfBrick(HAPManualDefinitionWithTaskInterfaceInteractive.TASKINTERFACE);  }
	@Override
	public void setTaskInterface(HAPEntityOrReference taskInterface) {    this.setAttributeValueWithBrick(HAPManualDefinitionWithTaskInterfaceInteractive.TASKINTERFACE, taskInterface);       }
 
	public HAPResourceId getScriptResourceId() {   return (HAPResourceId)this.getAttributeValueOfValue(HAPBlockTaskTaskScript.SCRIPTRESOURCEID);  }
	public void setScriptResourceId(HAPResourceId resourceId) {    this.setAttributeValueWithValue(HAPBlockTaskTaskScript.SCRIPTRESOURCEID, resourceId);      }
	
}
