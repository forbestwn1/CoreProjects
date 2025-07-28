package com.nosliw.core.application.common.interactive;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPBrick;

public interface HAPWithBlockInteractiveTask extends HAPBrick{

	@HAPAttribute
	public static String TASKINTERFACE = "taskInterface";
	
	public HAPEntityOrReference getTaskInterface();

}
