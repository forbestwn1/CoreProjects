package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPPluginEntityProcessorComplex {

	String getEntityType();
	
	//process definition
	void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);

	//new executable
	HAPExecutableEntityComplex newExecutable();
	
}
