package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;

public interface HAPManualWithBrick {

	//entity definition
	public static final String BRICK = "brick";

	public static final String BRICKTYPEID = "brickTypeId";

	HAPManualBrick getBrick();
	
	HAPIdBrickType getBrickTypeId();
	
}
