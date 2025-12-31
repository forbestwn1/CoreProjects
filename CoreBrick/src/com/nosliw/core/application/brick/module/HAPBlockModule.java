package com.nosliw.core.application.brick.module;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.brick.container.HAPBrickContainer;

@HAPEntityWithAttribute
public interface HAPBlockModule {

	@HAPAttribute
	public static String TASK = "task";

	@HAPAttribute
	public static String COMMAND = "command";

	@HAPAttribute
	public static String PAGE = "page";

	HAPBrickContainer getTasks();

	HAPBrickContainer getCommands();

	HAPBrickContainer getPages();
	
}
