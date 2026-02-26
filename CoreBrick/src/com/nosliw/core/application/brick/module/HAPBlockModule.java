package com.nosliw.core.application.brick.module;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.common.task.HAPWithBrickTasks;

@HAPEntityWithAttribute
public interface HAPBlockModule extends HAPWithBrickTasks{

	@HAPAttribute
	public static String COMMAND = "command";

	@HAPAttribute
	public static String PAGE = "page";

	HAPBrickContainer getCommands();

	HAPBrickContainer getPages();
	
}
