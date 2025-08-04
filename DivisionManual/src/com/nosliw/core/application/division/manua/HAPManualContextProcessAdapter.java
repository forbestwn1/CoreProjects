package com.nosliw.core.application.division.manua;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualContextProcessAdapter extends HAPManualContextProcess{

	private HAPPath m_baseBrickPath;
	
	public HAPManualContextProcessAdapter(HAPBundle bundle, String rootBrickName, HAPPath baseBrickPath, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(bundle, rootBrickName, runtimeEnv, manualBrickMan);
		this.m_baseBrickPath = baseBrickPath;
	}
	
	public HAPPath getRootPathForBaseBrick() {    return this.m_baseBrickPath;    }
	
}
