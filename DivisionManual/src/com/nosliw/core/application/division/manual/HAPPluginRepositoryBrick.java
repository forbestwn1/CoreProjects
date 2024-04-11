package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;

public interface HAPPluginRepositoryBrick {

	HAPIdBrickType getBrickType();
	
	HAPManualBrick retrieveBrickDefinition(HAPIdBrick brickId);
	
}
