package com.nosliw.core.application;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;

public class HAPWrapperValueInAttributeReferenceResource extends HAPWrapperValueInAttribute{

	private HAPInfoResourceIdNormalize m_normalizedResourceId;
	
	public HAPWrapperValueInAttributeReferenceResource(HAPInfoResourceIdNormalize normalizedResourceId) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID);
		this.m_normalizedResourceId = normalizedResourceId;
	}
	
	@Override
	public Object getValue() {     return this.m_normalizedResourceId;      }

	public HAPInfoResourceIdNormalize getNormalizedResourceId() {   return this.m_normalizedResourceId;    }
	
}
