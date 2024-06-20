package com.nosliw.core.application.common.valueport;

import com.nosliw.common.info.HAPEntityInfoImp;

public class HAPWrapperValuePort extends HAPEntityInfoImp{

	private HAPValuePort m_valuePort;
	
	public HAPWrapperValuePort(HAPValuePort valuePort) {
		this.m_valuePort = valuePort;
	}
	
	public HAPValuePort getValuePort() {	return this.m_valuePort;	}
	
	
	
}
