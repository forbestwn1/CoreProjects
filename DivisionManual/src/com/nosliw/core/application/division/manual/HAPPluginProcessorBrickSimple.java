package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPBrick;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public interface HAPPluginProcessorBrickSimple extends HAPPluginProcessorBrick{

	String getEntityType();
	
	HAPBrick newExecutable();
	
	//process
	void process(HAPExecutableEntity entityExe, HAPManualContextProcess processContext);

}
