package com.nosliw.core.application.brick.interactive.interfacee.task;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;

@HAPEntityWithAttribute
public interface HAPBlockInteractiveInterfaceTask extends HAPBrick{

	@HAPAttribute
	public static String VALUE = "value";

	HAPInteractiveTask getValue();

}
