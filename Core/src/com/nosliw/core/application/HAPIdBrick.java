package com.nosliw.core.application;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

public class HAPIdBrick extends HAPSerializableImp{

	private HAPIdBrickType m_entityTypeId;
	
	//entity may store and process differently, 
	private String m_division;
	
	private String m_id;

	public HAPIdBrick() {}
	
	public HAPIdBrick(HAPIdBrickType entityTypeId, String division, String id) {
		this.m_entityTypeId = entityTypeId;
		this.m_division = division;
		this.m_id = id;
	}
	
	public HAPIdBrickType getEntityTypeId() {    return this.m_entityTypeId;     }
	public void setEntityTypeId(HAPIdBrickType entityTypeId) {    this.m_entityTypeId = entityTypeId;      }
	
	public String getDivision() {     return this.m_division;     }
	public void setDivision(String division) {     this.m_division = division;     }
	
	public String getId() {    return this.m_id;    }
	
	public String getKey() {
		return HAPUtilityNamingConversion.cascadeLevel2(new String[] {this.m_entityTypeId.getKey(), this.m_id, this.m_division});
	}
	
}
