package com.nosliw.data.core.task;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;

@HAPEntityWithAttribute
public interface HAPDefinitionTask extends HAPEntityInfo{

	@HAPAttribute
	public static String TASKTYPE = "taskType";
	
	String getTaskType();
	
	HAPDefinitionTask cloneTaskDefinition();
	
}
