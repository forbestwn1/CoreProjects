package com.nosliw.data.core.complex;

public class HAPIdEntityInDomain {

	private String m_entityId;

	private String m_entityType;
	
	public HAPIdEntityInDomain(String entityId) {
		this.m_entityId = entityId;
	}
	
	public String getEntityId() {	return this.m_entityId;	}
	
	public String getEntityType() {     return this.m_entityType;      }
	
}
