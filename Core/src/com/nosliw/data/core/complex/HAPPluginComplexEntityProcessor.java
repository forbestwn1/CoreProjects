package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPPluginComplexEntityProcessor {

	String getEntityType();
	
	//process definition
	void process(HAPIdEntityInDomain complexEntityDefinitionId, HAPContextProcessor processContext);

	//new executable
	HAPExecutableEntityComplex newExecutable();
	
}
