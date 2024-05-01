package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;

public class HAPManualContextProcessAdapter {

	private HAPBundle m_bundle;
	
	private HAPPath m_baseBrickPath;
	
	public HAPManualContextProcessAdapter(HAPBundle bundle, HAPPath baseBrickPath) {
		this.m_bundle = bundle;
		this.m_baseBrickPath = baseBrickPath;
	}
	
	public HAPBundle getCurrentBundle(){   return this.m_bundle;  }

	public HAPPath getRootPathForBaseBrick() {    return this.m_baseBrickPath;    }
	
}
