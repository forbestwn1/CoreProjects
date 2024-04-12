package com.nosliw.core.application;

public interface HAPPluginBrick {

	HAPInfoBrickType getBrickTypeInfo();
	
	HAPBrick newInstance();
	
}
