package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualContextProcessAdapter extends HAPManualContextProcess{

	private HAPPath m_baseBrickPath;
	
	public HAPManualContextProcessAdapter(HAPBundle bundle, String rootBrickName, HAPPath baseBrickPath, HAPManualManagerBrick manualBrickMan) {
		super(bundle, rootBrickName, manualBrickMan);
		this.m_baseBrickPath = baseBrickPath;
	}
	
	public HAPPath getRootPathForBaseBrick() {    return this.m_baseBrickPath;    }
	
}
