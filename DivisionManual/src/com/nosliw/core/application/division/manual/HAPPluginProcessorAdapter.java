package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPAdapter;
import com.nosliw.core.application.HAPIdBrickType;

public interface HAPPluginProcessorAdapter extends HAPPluginProcessorBlock{

	
	HAPIdBrickType getBrickType();
	
	//process
	void process(HAPAdapter adapterExe, HAPManualAdapter adapterDef, HAPManualContextProcessAdapter processContext);

}
