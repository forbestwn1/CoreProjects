package com.nosliw.core.application.brick.task.flow;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPValueOfDynamic;

@HAPEntityWithAttribute
public interface HAPBlockTaskFlowActivityDynamic extends HAPBlockTaskFlowActivity{

	@HAPAttribute
	public static final String DEFINITION = "definition";
	
	@HAPAttribute
	public static final String RUNTIME = "runtime";
	
	HAPValueOfDynamic getDefinition();
	
	HAPTaskFlowAddressTask getRuntime();
	
}
