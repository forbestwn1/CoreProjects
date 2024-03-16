package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPBrick;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public interface HAPPluginProcessorEntityDefinitionSimple extends HAPPluginProcessorEntityDefinition{

	String getEntityType();
	
	HAPBrick newExecutable();
	
	//process
	void process(HAPExecutableEntity entityExe, HAPContextProcess processContext);

}
