package com.nosliw.core.application.brick.task.flow;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPBrick;

public interface HAPBlockTaskFlowActivityTask extends HAPTaskFlowActivity, HAPBrick{

	@HAPAttribute
	public static final String TASK = "task";
	
	HAPEntityOrReference getTask();

}
