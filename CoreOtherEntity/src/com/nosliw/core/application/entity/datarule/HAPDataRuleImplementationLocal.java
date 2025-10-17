package com.nosliw.core.application.entity.datarule;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPDataRuleImplementationLocal implements HAPDataRuleImplementation{

	private String m_pathId;
	
	public HAPDataRuleImplementationLocal(String pathId) {
		this.m_pathId = pathId;
	}
	
	@Override
	public String getImmplementationType() {
		return HAPConstantShared.DATARULE_IMPLEMENTATION_LOCAL;
	}
	
}
