package com.nosliw.core.application.common.task;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.brick.container.HAPBrickContainer;

@HAPEntityWithAttribute
public interface HAPWithBrickTasks {

	@HAPAttribute
	public static String TASK = "task";

	HAPBrickContainer getTasks();

}
