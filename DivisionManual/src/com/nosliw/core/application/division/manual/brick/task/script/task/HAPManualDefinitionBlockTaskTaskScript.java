package com.nosliw.core.application.division.manual.brick.task.script.task;

import java.util.ArrayList;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.task.script.task.HAPBlockTaskTaskScript;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;
import com.nosliw.core.application.common.withvariable.HAPWithVariableDebugDefinition;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.entity.script.HAPWithScriptReference;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualDefinitionBlockTaskTaskScript extends HAPManualDefinitionBrick implements HAPWithBlockInteractiveTask, HAPWithScriptReference{

	public HAPManualDefinitionBlockTaskTaskScript() {
		super(HAPEnumBrickType.TASK_TASK_SCRIPT_100);
		this.setAttributeValueWithValue(HAPWithVariableDebugDefinition.VARIABLE, new ArrayList<String>());
	}

	@Override
	public HAPEntityOrReference getTaskInterface() {    return this.getAttributeValueOfBrick(HAPWithBlockInteractiveTask.TASKINTERFACE);  }
	@Override
	public void setTaskInterface(HAPEntityOrReference taskInterface) {    this.setAttributeValueWithBrick(HAPWithBlockInteractiveTask.TASKINTERFACE, taskInterface);       }
 	
	@Override
	public HAPResourceId getScriptResourceId() {   return (HAPResourceId)this.getAttributeValueOfValue(HAPBlockTaskTaskScript.SCRIPTRESOURCEID);  }
	public void setScriptResourceId(HAPResourceId resourceId) {    this.setAttributeValueWithValue(HAPBlockTaskTaskScript.SCRIPTRESOURCEID, resourceId);      }
	
	public Object getExtra() {   return this.getAttributeValueOfValue(HAPBlockTaskTaskScript.EXTRA);  }
	public void setExtra(Object extra) {    this.setAttributeValueWithValue(HAPBlockTaskTaskScript.EXTRA, extra);  }
}
