package com.nosliw.data.core.domain;

import com.nosliw.common.utils.HAPBasicUtility;

//id for entity in domain
public class HAPIdEntityInDomain {

	//entity id
	private String m_entityId;

	//entity type
	private String m_entityType;
	
	public HAPIdEntityInDomain(String entityId, String entityType) {
		this.m_entityId = entityId;
		this.m_entityType = entityType;
	}
	
	public String getEntityId() {	return this.m_entityId;	}
	
	public String getEntityType() {     return this.m_entityType;      }

	public HAPIdEntityInDomain cloneIdEntityInDomain() {
		return new HAPIdEntityInDomain(this.m_entityId, this.m_entityType);
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean out = false;
		if(obj instanceof HAPIdEntityInDomain) {
			HAPIdEntityInDomain entityId = (HAPIdEntityInDomain)obj;
			out = HAPBasicUtility.isEquals(entityId.getEntityId(), this.getEntityId()) && HAPBasicUtility.isEquals(entityId.getEntityType(), this.getEntityType());
		}
		return out;
	}
	
}
