package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPPluginEntityProcessorSimple {

	String getEntityType();
	
	//process definition
	HAPExecutableEntity process(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext);

}
