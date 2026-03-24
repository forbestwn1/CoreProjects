package com.nosliw.core.application.brick.wrappertask;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBrick;

@HAPEntityWithAttribute
public interface HAPBlockTaskWrapper extends HAPBrick{

	public static final String CHILD_TASK = "task";

	@HAPAttribute
	public static String TASK = HAPConstantShared.PREFIX_BRICKATTRIBUTE_INTERPRET + HAPConstantShared.PREFIX_BRICKATTRIBUTE_FACTORY + "__task__";
	
	@HAPAttribute
	public static String TASKTYPE = "taskType";
	
	String getTaskType();
	
	HAPEntityOrReference getTask();
}
