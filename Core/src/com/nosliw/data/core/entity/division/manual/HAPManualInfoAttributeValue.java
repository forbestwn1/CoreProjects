package com.nosliw.data.core.entity.division.manual;

import com.nosliw.data.core.entity.HAPInfoEntityType;

public class HAPManualInfoAttributeValue {

	public static final String ENTITYTYPE = "entityType";
	
	private String m_valueType;
	
	private HAPInfoEntityType m_entityTypeInfo;

	public HAPManualInfoAttributeValue(String valueType, HAPInfoEntityType entityTypeInfo) {
		this.m_valueType = valueType;
		this.m_entityTypeInfo = entityTypeInfo;
	}
	
	
	
	public String getValueType() {   return this.m_valueType;  }
	
	public HAPInfoEntityType getEntityTypeInfo() {   return this.m_entityTypeInfo;   }
	
	
	
}
