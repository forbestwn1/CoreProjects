package com.nosliw.core.application.division.manual;

import com.nosliw.common.info.HAPEntityInfoImp;

public class HAPManualInfoAdapter extends HAPEntityInfoImp{

	private HAPManualWrapperValue m_valueWrapper;
	
	public HAPManualInfoAdapter(HAPManualWrapperValue valueWrapper) {
		
	}

	public HAPManualWrapperValue getValueWrapper() {    return this.m_valueWrapper;      }
	
}
