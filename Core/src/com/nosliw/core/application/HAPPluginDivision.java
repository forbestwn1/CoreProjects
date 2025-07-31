package com.nosliw.core.application;

import java.util.Set;

public interface HAPPluginDivision {

	String getName();
	
	HAPBundle getBundle(HAPIdBrick brickId);

	//what brick type related with this division
	Set<HAPIdBrickType> getBrickTypes();
}
