package com.nosliw.core.application;

import java.util.List;

public interface HAPPluginBrick {

	HAPInfoBrickType getBrickTypeInfo();
	
	HAPBrick newInstance();

	//what kind of embeded resource to expose
	List<HAPInfoExportResource> getExposeResourceInfo(HAPBrick brick);
}
