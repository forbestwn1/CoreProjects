package com.nosliw.core.application;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.division.manual.executable.HAPBrick;

@HAPEntityWithAttribute
public interface HAPWithBrick {

	@HAPAttribute
	public static final String BRICK = "brick";

	HAPBrick getBrick();
	
}
