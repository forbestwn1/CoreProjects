package com.nosliw.core.application.division.manual;

import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public interface HAPPluginProcessorBrickSimple extends HAPPluginProcessorBrick{

	String getEntityType();
	
	//process
	void process(HAPExecutableEntity entityExe, HAPManualContextProcess processContext);

}
