package com.nosliw.core.application.common.interactive;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.interfac.HAPEntityOrReference;

public interface HAPWithBlockInteractiveTask{

	@HAPAttribute
	public static String TASKINTERFACE = "taskInterface";
	
	public HAPEntityOrReference getTaskInterface();

}
