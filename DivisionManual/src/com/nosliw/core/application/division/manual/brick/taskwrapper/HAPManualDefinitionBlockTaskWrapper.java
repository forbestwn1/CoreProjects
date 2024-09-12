package com.nosliw.core.application.division.manual.brick.taskwrapper;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockTaskWrapper extends HAPManualDefinitionBrick{

	public static final String TASKBRICKTYPE = "taskBrickType";
	
	public HAPManualDefinitionBlockTaskWrapper() {
		super(HAPEnumBrickType.TASKWRAPPER_100);
	}

	public HAPIdBrickType getTaskBrickType() {    return (HAPIdBrickType)this.getAttributeValueOfValue(TASKBRICKTYPE);    }
	public void setTaskBrickType(HAPIdBrickType taskType) {    this.setAttributeValueWithValue(TASKBRICKTYPE, taskType);     }
	
	public void setTask(HAPEntityOrReference entityOrRef) {      this.setAttributeValueWithBrick(HAPManualBlockTaskWrapper.TASK, entityOrRef);        }
	public HAPEntityOrReference getTask() {     return this.getAttributeValueOfBrick(HAPManualBlockTaskWrapper.TASK);        }
	
}
