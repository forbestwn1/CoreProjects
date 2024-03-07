package com.nosliw.data.core.entity.division.manual;

import com.nosliw.data.core.entity.HAPEntityBundle;

public class HAPContextProcess {

	private HAPEntityBundle m_bundle;
	
	public HAPContextProcess(HAPEntityBundle bundle) {
		this.m_bundle = bundle;
	}
	
	public HAPEntityBundle getCurrentBundle(){   return this.m_bundle;  }
	
}
