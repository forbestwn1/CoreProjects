package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;

public class HAPReferenceExternal {

	private HAPInfoResourceIdNormalize m_normalizedResourceId;
	
	public HAPReferenceExternal(HAPInfoResourceIdNormalize normalizedResourceId) {
		this.m_normalizedResourceId = normalizedResourceId;
	}
	
	public HAPInfoResourceIdNormalize getNormalizedResourceId() {   return this.m_normalizedResourceId;    }
}
