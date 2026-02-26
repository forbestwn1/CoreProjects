package com.nosliw.core.application.common.task;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.application.brick.container.HAPBrickContainer;

public interface HAPWithBrickTasks {

	@HAPAttribute
	public static String TASK = "task";

	HAPBrickContainer getTasks();

}
