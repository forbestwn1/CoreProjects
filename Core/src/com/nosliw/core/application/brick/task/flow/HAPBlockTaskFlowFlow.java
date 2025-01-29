package com.nosliw.core.application.brick.task.flow;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.common.interactive.HAPWithBlockInteractiveTask;

public interface HAPBlockTaskFlowFlow extends HAPBrick, HAPWithBlockInteractiveTask{

	@HAPAttribute
	public static final String START = "start";

	@HAPAttribute
	public static final String ENDS = "ends";
	
	@HAPAttribute
	public static final String ACTIVITY = "activity";

	
	
}
