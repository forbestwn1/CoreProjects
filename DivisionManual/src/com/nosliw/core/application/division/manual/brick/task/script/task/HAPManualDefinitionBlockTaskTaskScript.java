package com.nosliw.core.application.division.manual.brick.task.script.task;

import java.util.ArrayList;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.parm.HAPParms;
import com.nosliw.common.parm.HAPWithParms;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.task.script.task.HAPBlockTaskTaskScript;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;
import com.nosliw.core.application.common.withvariable.HAPWithVariableDebugDefinition;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.entity.script.HAPWithScriptReference;
import com.nosliw.core.resource.HAPResourceId;

public class HAPManualDefinitionBlockTaskTaskScript extends HAPManualDefinitionBrick implements HAPWithBlockInteractiveTask, HAPWithScriptReference, HAPWithParms{

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
	
	@Override
	public HAPParms getParms() {  return (HAPParms)this.getAttributeValueOfValue(HAPWithParms.PARM);  }

	@Override
	public void setParms(HAPParms parms) {    this.setAttributeValueWithValue(HAPWithParms.PARM, parms);    }

}
