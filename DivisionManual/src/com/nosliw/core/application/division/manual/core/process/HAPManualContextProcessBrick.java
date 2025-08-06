package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualContextProcessBrick extends HAPManualContextProcess{

	public HAPManualContextProcessBrick(HAPBundle bundle, String rootBrickName, HAPManualManagerBrick manualBrickMan, HAPManagerApplicationBrick brickMan) {
		super(bundle, rootBrickName, manualBrickMan, brickMan);
	}
	
}
