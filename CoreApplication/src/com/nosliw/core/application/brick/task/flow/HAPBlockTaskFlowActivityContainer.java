package com.nosliw.core.application.brick.task.flow;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPBlockTaskFlowActivityContainer extends HAPBlockTaskFlowActivity{

	@HAPAttribute
	public static final String CHILD = "child";
	

}
