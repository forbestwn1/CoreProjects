package com.nosliw.data.core.domain;

public class HAPIdEntityInDomain {

	private String m_entityId;

	private String m_entityType;
	
	public HAPIdEntityInDomain(String entityId, String entityType) {
		this.m_entityId = entityId;
		this.m_entityType = entityType;
	}
	
	public String getEntityId() {	return this.m_entityId;	}
	
	public String getEntityType() {     return this.m_entityType;      }

	
	
}
