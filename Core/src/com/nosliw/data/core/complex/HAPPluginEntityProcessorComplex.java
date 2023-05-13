package com.nosliw.data.core.complex;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

public interface HAPPluginEntityProcessorComplex {

	String getEntityType();
	
	//process definition
	void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);

	//new executable
	HAPExecutableEntityComplex newExecutable();
	
}
