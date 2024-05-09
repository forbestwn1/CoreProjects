package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPBrickAdapter;
import com.nosliw.core.application.HAPIdBrickType;

public interface HAPPluginProcessorAdapter{

	
	HAPIdBrickType getBrickType();
	
	//process
	void process(HAPBrickAdapter adapterExe, HAPManualAdapter adapterDef, HAPManualContextProcessAdapter processContext);

}
