package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPManualContextProcessAdapter extends HAPManualContextProcess{

	private HAPPath m_baseBrickPath;
	
	public HAPManualContextProcessAdapter(HAPBundle bundle, 
			String rootBrickName, 
			HAPPath baseBrickPath, 
			HAPManualManagerBrick manualBrickMan, 
			HAPManagerApplicationBrick brickMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPManagerResource resourceMan,
			HAPRuntimeInfo runtimeInfo) {
		super(bundle, rootBrickName, manualBrickMan, brickMan, dataTypeHelper, resourceMan, runtimeInfo);
		this.m_baseBrickPath = baseBrickPath;
	}
	
	public HAPPath getRootPathForBaseBrick() {    return this.m_baseBrickPath;    }
	
}
