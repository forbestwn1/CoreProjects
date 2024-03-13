package com.nosliw.data.core.entity.division.manual;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualInfoAttributeValueReferenceResource extends HAPManualInfoAttributeValue{

	//resource id
	public static final String RESOURCEID = "resourceId";

	//reference to external resource
	private HAPResourceId m_resourceId;

	public HAPManualInfoAttributeValueReferenceResource(HAPResourceId resourceId) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE);
		this.m_resourceId = resourceId;
	}

	public HAPResourceId getResourceId() {    return this.m_resourceId;     }
	
	
}
