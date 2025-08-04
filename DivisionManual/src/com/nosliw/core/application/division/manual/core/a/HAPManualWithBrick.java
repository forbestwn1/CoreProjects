package com.nosliw.core.application.division.manual.core.a;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;

public interface HAPManualWithBrick {

	//entity definition
	public static final String BRICK = "brick";

	public static final String BRICKTYPEID = "brickTypeId";

	HAPManualDefinitionBrick getBrick();
	
	HAPIdBrickType getBrickTypeId();
	
}
