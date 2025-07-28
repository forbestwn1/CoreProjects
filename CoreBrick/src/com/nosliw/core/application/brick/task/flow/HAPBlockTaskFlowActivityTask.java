package com.nosliw.core.application.brick.task.flow;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;

@HAPEntityWithAttribute
public interface HAPBlockTaskFlowActivityTask extends HAPBlockTaskFlowActivity{

	@HAPAttribute
	public static final String TASK = "task";
	
	HAPEntityOrReference getTask();

}
