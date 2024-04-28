package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPAdapter;

public interface HAPPluginProcessorAdapter extends HAPPluginProcessorBlock{

	//process
	void process(HAPAdapter adapterExe, HAPManualBrickAdapter adapterDef, HAPManualContextProcessAdapter processContext);

}
