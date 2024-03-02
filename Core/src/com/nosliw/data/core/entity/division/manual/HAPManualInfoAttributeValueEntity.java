package com.nosliw.data.core.entity.division.manual;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.HAPInfoEntityType;

public class HAPManualInfoAttributeValueEntity extends HAPManualInfoAttributeValue{

	//entity definition
	public static final String ENTITY = "entity";

	private HAPManualEntity m_entity;
	
	public HAPManualInfoAttributeValueEntity(HAPInfoEntityType entityTypeInfo, HAPManualEntity entity) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_ENTITY, entityTypeInfo);
		this.m_entity = entity;
	}

	public HAPManualEntity getEntity() {    return this.m_entity;    }
	
}
