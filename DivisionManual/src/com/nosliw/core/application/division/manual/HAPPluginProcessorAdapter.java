package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickAdapter;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickAdapter;

public interface HAPPluginProcessorAdapter{

	
	HAPIdBrickType getBrickType();
	
	//process
	void process(HAPManualBrickAdapter adapterExe, HAPManualDefinitionBrickAdapter adapterDef, HAPManualContextProcessAdapter processContext);

}
