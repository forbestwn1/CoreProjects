package com.nosliw.data.core.entity;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPIdEntity extends HAPSerializableImp{

	private HAPIdEntityType m_entityTypeId;
	
	//entity may store and process differently, 
	private String m_division;
	
	private String m_id;

	public HAPIdEntity() {}
	
	public HAPIdEntity(HAPIdEntityType entityTypeId, String division, String id) {
		this.m_entityTypeId = entityTypeId;
		this.m_division = division;
		this.m_id = id;
	}
	
	public HAPIdEntityType getEntityTypeId() {    return this.m_entityTypeId;     }
	
	public String getDivision() {     return this.m_division;     }
	
	public String getId() {    return this.m_id;    }
	
}
