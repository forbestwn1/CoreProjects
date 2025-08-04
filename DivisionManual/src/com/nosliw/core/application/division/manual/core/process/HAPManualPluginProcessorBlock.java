package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;

public class HAPManualPluginProcessorBlock extends HAPManualPluginProcessorBrick{

	public HAPManualPluginProcessorBlock(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass) {
		super(brickType, brickClass);
	}

}
