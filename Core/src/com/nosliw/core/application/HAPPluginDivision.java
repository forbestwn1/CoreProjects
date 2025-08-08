package com.nosliw.core.application;

import java.util.Set;

import com.nosliw.core.runtime.HAPRuntimeInfo;

public interface HAPPluginDivision {

	String getDivisionName();
	
	HAPBundle getBundle(HAPIdBrick brickId, HAPRuntimeInfo runtimeInfo);

	//what brick type related with this division
	Set<HAPIdBrickType> getBrickTypes();
}
