package com.nosliw.core.application.brick.task.flow;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPAddressValue;
import com.nosliw.core.application.HAPValueOfDynamic;

@HAPEntityWithAttribute
public interface HAPBlockTaskFlowActivityDynamic extends HAPBlockTaskFlowActivity{

	@HAPAttribute
	public static final String TASK = "task";
	
	@HAPAttribute
	public static final String TASKADDRESS = "taskAddress";
	
	HAPValueOfDynamic getTask();
	
	HAPAddressValue getTaskAddress();
	
}
