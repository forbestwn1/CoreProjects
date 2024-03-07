package com.nosliw.data.core.entity;

public class HAPInfoAttributeValueEntity extends HAPInfoAttributeValue{

	private HAPEntityExecutable m_entity;
	
	public HAPInfoAttributeValueEntity(HAPEntityExecutable entity) {
		this.m_entity = entity;
	}
	
	public HAPEntityExecutable getEntity() {    return this.m_entity;    }
}
