package com.nosliw.data.core.entity;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPInfoAttributeValueEntity extends HAPInfoAttributeValue implements HAPWithEntity{

	private HAPEntityExecutable m_entity;
	
	private HAPIdEntityType m_entityTypeId;
	
	public HAPInfoAttributeValueEntity(HAPEntityExecutable entity) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_ENTITY);
		this.m_entity = entity;
		this.m_entityTypeId = this.m_entity.getEntityTypeId();
	}
	
	@Override
	public HAPEntityExecutable getEntity() {    return this.m_entity;    }

	@Override
	public HAPIdEntityType getEntityTypeId() {   return this.m_entityTypeId;  }
	
}
