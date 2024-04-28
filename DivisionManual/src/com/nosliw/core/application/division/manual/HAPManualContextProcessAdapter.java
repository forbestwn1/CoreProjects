package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;

public class HAPManualContextProcessAdapter {

	private HAPBundle m_bundle;
	
	public HAPManualContextProcessAdapter(HAPBundle bundle) {
		this.m_bundle = bundle;
	}
	
	public HAPBundle getCurrentBundle(){   return this.m_bundle;  }

	public HAPPath getRootPathForBaseBrick() {}
	
}
