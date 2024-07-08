package com.nosliw.core.application.brick.taskwrapper;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.division.manual.executable.HAPBrickBlockSimple;

@HAPEntityWithAttribute
public class HAPBlockTaskWrapper extends HAPBrickBlockSimple{

	@HAPAttribute
	public static String TASK = "task";
	
	@HAPAttribute
	public static String TASKTYPE = "taskType";
	

	public String getTaskType() {	return (String)this.getAttributeValueOfValue(TASKTYPE);	}
	public void setTaskType(String taskType) {    this.setAttributeValueWithValue(TASKTYPE, taskType);     }
	
}
