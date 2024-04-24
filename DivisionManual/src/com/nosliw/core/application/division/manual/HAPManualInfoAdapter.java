package com.nosliw.core.application.division.manual;

import com.nosliw.common.info.HAPEntityInfoImp;

public class HAPManualInfoAdapter extends HAPEntityInfoImp{

	//attribute value
	private HAPManualWrapperValueInAttribute m_valueInfo;
	
	public HAPManualInfoAdapter(HAPManualWrapperValueInAttribute valueInfo) {
		this.m_valueInfo = valueInfo;
	}
	
	public HAPManualWrapperValueInAttribute getValueWrapper() {    return this.m_valueInfo;      }
	
}
