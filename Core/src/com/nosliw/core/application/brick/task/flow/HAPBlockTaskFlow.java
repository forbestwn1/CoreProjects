package com.nosliw.core.application.brick.task.flow;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.application.HAPBrick;

public interface HAPBlockTaskFlow extends HAPBrick{

	@HAPAttribute
	public static final String START = "start";

	@HAPAttribute
	public static final String ENDS = "ends";
	
	@HAPAttribute
	public static final String ACTIVITY = "activity";

	
	
}
