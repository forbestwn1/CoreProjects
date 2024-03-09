package com.nosliw.data.core.entity;

public class HAPInfoAttributeValueEntity extends HAPInfoAttributeValue implements HAPWithEntity{

	private HAPEntityExecutable m_entity;
	
	public HAPInfoAttributeValueEntity(HAPEntityExecutable entity) {
		this.m_entity = entity;
	}
	
	@Override
	public HAPEntityExecutable getEntity() {    return this.m_entity;    }
}
