package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;

public class HAPManualUtilityBrick {

	public static boolean isBrickComplex(HAPIdBrickType brickTypeId, HAPManualManagerBrick manualBrickMan) {
		return manualBrickMan.getBrickTypeInfo(brickTypeId).getIsComplex();
	}

	
	
}
