package com.nosliw.core.application.common.interactive;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithInteractiveTask {

	@HAPAttribute
	public static final String TASKINTERACTIVE = "taskInteractive";

	HAPInteractiveTask getTaskInteractive();
	
}
