package com.nosliw.data.core.entity.division.manual;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.HAPInfoEntityType;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPManualInfoValueWithReferenceResource extends HAPManualInfoValue{

	//resource id
	public static final String RESOURCEID = "resourceId";

	//reference to external resource
	private HAPResourceId m_resourceId;

	private HAPManualEntity m_entity;

	public HAPManualInfoValueWithReferenceResource(HAPInfoEntityType entityTypeInfo, HAPResourceId resourceId) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_RESOURCEREFERENCE, entityTypeInfo);
		this.m_resourceId = resourceId;
	}

	
}
