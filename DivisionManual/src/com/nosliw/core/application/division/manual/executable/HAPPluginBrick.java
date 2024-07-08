package com.nosliw.core.application.division.manual.executable;

import java.util.List;

import com.nosliw.core.application.HAPInfoExportResource;

public interface HAPPluginBrick {

	HAPInfoBrickType getBrickTypeInfo();
	
	HAPManualBrick newInstance();

	//what kind of embeded resource to expose
	List<HAPInfoExportResource> getExposeResourceInfo(HAPManualBrick brick);
}
