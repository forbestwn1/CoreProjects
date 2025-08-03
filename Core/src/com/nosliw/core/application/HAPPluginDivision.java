package com.nosliw.core.application;

import java.util.Set;

public interface HAPPluginDivision {

	String getDivisionName();
	
	HAPBundle getBundle(HAPIdBrick brickId);

	//what brick type related with this division
	Set<HAPIdBrickType> getBrickTypes();
}
