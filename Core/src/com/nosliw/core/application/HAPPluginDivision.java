package com.nosliw.core.application;

import java.util.Set;

public interface HAPPluginDivision {

	HAPBundle getBundle(HAPIdBrick brickId);

	
	
	//what brick type related with this division
	Set<HAPIdBrickType> getBrickTypes();
}
