package com.nosliw.data.core.entity.division.manual;

import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.entity.HAPEntityExecutable;

public interface HAPPluginProcessorEntityDefinitionSimple extends HAPPluginProcessorEntityDefinition{

	String getEntityType();
	
	HAPEntityExecutable newExecutable();
	
	//process
	void process(HAPExecutableEntity entityExe, HAPContextProcess processContext);

}
