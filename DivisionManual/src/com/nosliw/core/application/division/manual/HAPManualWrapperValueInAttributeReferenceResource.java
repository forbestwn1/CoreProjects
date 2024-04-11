package com.nosliw.core.application.division.manual;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualWrapperValueInAttributeReferenceResource extends HAPManualWrapperValueInAttribute{

	//resource id
	public static final String RESOURCEID = "resourceId";

	//reference to external resource
	private HAPResourceId m_resourceId;

	public HAPManualWrapperValueInAttributeReferenceResource(HAPResourceId resourceId) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE);
		this.m_resourceId = resourceId;
	}

	public HAPResourceId getResourceId() {    return this.m_resourceId;     }
	
	
}
