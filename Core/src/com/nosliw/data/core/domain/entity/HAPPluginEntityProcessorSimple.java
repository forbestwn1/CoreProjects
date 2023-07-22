package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;

public interface HAPPluginEntityProcessorSimple {

	String getEntityType();
	
	HAPExecutableEntity newExecutable();
	
	//process
	void process(HAPExecutableEntity entityExe, HAPContextProcessor processContext);

}
