package com.nosliw.core.application;

import java.util.List;

public interface HAPPluginBrick {

	HAPInfoBrickType getBrickTypeInfo();
	
	HAPBrick newInstance();

	List<HAPInfoExportResource> getExposeResourceInfo(HAPBrick brick);
}
