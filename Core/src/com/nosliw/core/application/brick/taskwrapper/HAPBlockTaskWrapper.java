package com.nosliw.core.application.brick.taskwrapper;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;

@HAPEntityWithAttribute
public interface HAPBlockTaskWrapper extends HAPBrick{

	@HAPAttribute
	public static String TASK = "task";
	
	@HAPAttribute
	public static String TASKTYPE = "taskType";
	
	String getTaskType();
	


}
