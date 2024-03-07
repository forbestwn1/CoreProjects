package com.nosliw.data.core.entity.division.manual;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.entity.HAPInfoEntityType;

public class HAPManualInfoAttributeValueWithEntity extends HAPManualInfoAttributeValue{

	private HAPManualEntity m_entity;

	public HAPManualInfoAttributeValueWithEntity(String valueType, HAPInfoEntityType entityTypeInfo) {
		super(valueType, entityTypeInfo);
	}
	
	public HAPManualEntity getEntity() {	return this.m_entity;	}
	
	public void setEntity(HAPManualEntity entity) {    this.m_entity = entity;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUETYPE, m_valueType);
		if(this.m_entityTypeInfo!=null) {
			jsonMap.put(ENTITYTYPE, this.m_entityTypeInfo.toStringValue(HAPSerializationFormat.JSON));
		}
	}
}
