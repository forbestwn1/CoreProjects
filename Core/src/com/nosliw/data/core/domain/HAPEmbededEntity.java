package com.nosliw.data.core.domain;

public class HAPEmbededEntity {

	private HAPIdEntityInDomain m_entityId;
	
	private Object m_adapter;
	
	public Object getAdapter() {
		return m_adapter;
	}

	public void setAdapter(Object adapter) {
		this.m_adapter = adapter;
	}

	public HAPEmbededEntity(HAPIdEntityInDomain entityId) {
		this.m_entityId = entityId;
	}
	
	public HAPIdEntityInDomain getEntityId() {
		return this.m_entityId;
	}
	
	public void setEntityId(HAPIdEntityInDomain entityId) {
		this.m_entityId = entityId;
	}
	
	
	
}
