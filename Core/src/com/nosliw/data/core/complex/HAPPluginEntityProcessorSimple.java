package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;

public interface HAPPluginEntityProcessorSimple {

	String getEntityType();
	
	//process definition
	HAPExecutableEntity process(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext);

}
