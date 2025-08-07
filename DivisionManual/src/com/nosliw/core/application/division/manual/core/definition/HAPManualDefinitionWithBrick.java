package com.nosliw.core.application.division.manual.core.definition;

import com.nosliw.core.application.HAPIdBrickType;

public interface HAPManualDefinitionWithBrick {

	//entity definition
	public static final String BRICK = "brick";

	public static final String BRICKTYPEID = "brickTypeId";

	HAPManualDefinitionBrick getBrick();
	
	HAPIdBrickType getBrickTypeId();
	
}
