package com.nosliw.data.core.component;

import com.nosliw.data.core.process.HAPDefinitionProcessWrapper;

public interface HAPWithProcess {

	HAPDefinitionProcessWrapper getProcess(String name);
	
}
