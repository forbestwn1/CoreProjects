package com.nosliw.core.application.brick.task.flow;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.application.HAPBrick;

public interface HAPBlockTaskFlowNext extends HAPBrick{

	@HAPAttribute
	public static final String DECISION = "decision";

	@HAPAttribute
	public static final String TARGET = "target";


}
