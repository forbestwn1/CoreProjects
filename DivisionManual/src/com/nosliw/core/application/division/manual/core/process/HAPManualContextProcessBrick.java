package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPManualContextProcessBrick extends HAPManualContextProcess{

	public HAPManualContextProcessBrick(
			HAPBundle bundle, 
			String rootBrickName, 
			HAPManualManagerBrick manualBrickMan, 
			HAPManagerApplicationBrick brickMan, 
			HAPDataTypeHelper dataTypeHelper,
			HAPManagerResource resourceMan, 
			HAPRuntimeInfo runtimeInfo) {
		super(bundle, rootBrickName, manualBrickMan, brickMan, dataTypeHelper, resourceMan, runtimeInfo);
	}
	
}
