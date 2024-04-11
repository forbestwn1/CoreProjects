package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPBundle;

public class HAPManualContextProcess {

	private HAPBundle m_bundle;
	
	public HAPManualContextProcess(HAPBundle bundle) {
		this.m_bundle = bundle;
	}
	
	public HAPBundle getCurrentBundle(){   return this.m_bundle;  }
	
}
