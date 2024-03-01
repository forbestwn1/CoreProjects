package com.nosliw.data.core.entity.division.manual;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.HAPIdEntity;
import com.nosliw.data.core.entity.HAPInfoEntityType;

public class HAPManualInfoValueWithReferenceEntity extends HAPManualInfoValue{

	//reference to attachment
	public static final String ENTITYREFERENCE = "entityReference";

	//reference to local resource
	private HAPIdEntity m_localEntityId;

	private HAPManualEntity m_entity;

	public HAPManualInfoValueWithReferenceEntity(HAPInfoEntityType entityTypeInfo, HAPIdEntity localEntityId) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_ENTITYREFERENCE, entityTypeInfo);
		this.m_localEntityId = localEntityId;
	}

	public void setReferencedEntity(HAPManualEntity entity) {
		this.m_entity = entity;
	}
	
}
