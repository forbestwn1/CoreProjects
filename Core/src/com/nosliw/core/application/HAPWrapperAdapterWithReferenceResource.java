package com.nosliw.core.application;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPWrapperAdapterWithReferenceResource extends HAPWrapperAdapter{

	public static final String RESOURCEID = "resourceId";

	private HAPResourceId m_resourceId;

	public HAPWrapperAdapterWithReferenceResource(HAPResourceId resourceId) {
		this.m_resourceId = resourceId;
	}
	
	@Override
	public String getValueType() {   return HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_RESOURCEID;  }

	public HAPResourceId getResourceId() {    return this.m_resourceId;     }
	
}
