package com.nosliw.core.application.division.manual.brick.wrappertask;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;

public class HAPManualDefinitionBlockTaskWrapper extends HAPManualDefinitionBrick{

	public static final String TASKBRICKTYPE = "taskBrickType";
	
	public HAPManualDefinitionBlockTaskWrapper() {
		super(HAPEnumBrickType.TASKWRAPPER_100);
	}

	public HAPIdBrickType getTaskBrickType() {    return (HAPIdBrickType)this.getAttributeValueOfValue(TASKBRICKTYPE);    }
	public void setTaskBrickType(HAPIdBrickType taskType) {    this.setAttributeValueWithValue(TASKBRICKTYPE, taskType);     }
	
	public void setTask(HAPEntityOrReference entityOrRef) {      this.setAttributeValueWithBrick(HAPBlockTaskWrapper.TASK, entityOrRef);        }
	public HAPEntityOrReference getTask() {     return this.getAttributeValueOfBrick(HAPBlockTaskWrapper.TASK);        }
	
}
