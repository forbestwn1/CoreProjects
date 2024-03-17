package com.nosliw.core.application;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithBrick {

	@HAPAttribute
	public static final String BRICK = "brick";

	@HAPAttribute
	public static final String BRICKTYPEID = "brickTypeId";

	HAPBrick getBrick();
	
	HAPIdBrickType getBrickTypeId();
	
}
