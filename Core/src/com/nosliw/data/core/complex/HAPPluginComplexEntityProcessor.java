package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPPluginComplexEntityProcessor {

	//process definition
	HAPIdEntityInDomain process(HAPIdEntityInDomain definitionEntityId, HAPContextProcessor processContext);

	//new executable
	HAPExecutableEntityComplex newExecutable();
	
}
