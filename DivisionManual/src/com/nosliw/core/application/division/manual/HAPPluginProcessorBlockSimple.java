package com.nosliw.core.application.division.manual;

import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public interface HAPPluginProcessorBlockSimple extends HAPPluginProcessorBlock{

	String getEntityType();
	
	//process
	void process(HAPExecutableEntity entityExe, HAPManualContextProcessBrick processContext);

}
