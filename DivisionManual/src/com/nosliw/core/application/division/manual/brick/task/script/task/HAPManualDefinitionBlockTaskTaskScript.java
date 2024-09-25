package com.nosliw.core.application.division.manual.brick.task.script.task;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.task.script.task.HAPBlockTaskTaskScript;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualDefinitionBlockTaskTaskScript extends HAPManualDefinitionBrick{

	protected HAPManualDefinitionBlockTaskTaskScript() {
		super(HAPEnumBrickType.TASK_EXPRESSION_SCRIPT_100);
	}

	public HAPEntityOrReference getTaskInterface() {    return this.getAttributeValueOfBrick(HAPBlockTaskTaskScript.TASKINTERFACE);  }
	public void setTaskInterface(HAPEntityOrReference taskInterface) {    this.setAttributeValueWithBrick(HAPBlockTaskTaskScript.TASKINTERFACE, taskInterface);       }

	public HAPResourceId getScriptResourceId() {   return (HAPResourceId)this.getAttributeValueOfValue(HAPBlockTaskTaskScript.SCRIPTRESOURCEID);  }
	public void setScriptResourceId(HAPResourceId resourceId) {    this.setAttributeValueWithValue(HAPBlockTaskTaskScript.SCRIPTRESOURCEID, resourceId);      }
	
}
