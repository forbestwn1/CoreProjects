package com.nosliw.data.core.entity.division.manual;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.entity.HAPIdEntityType;

public class HAPManualInfoAttributeValueWithEntity extends HAPManualInfoAttributeValue{

	public static final String ENTITY = "entity";
	
	private HAPManualEntity m_entity;

	public HAPManualInfoAttributeValueWithEntity(String valueType, HAPIdEntityType entityTypeId) {
		super(valueType, entityTypeId);
	}
	
	public HAPManualEntity getEntity() {	return this.m_entity;	}
	
	public void setEntity(HAPManualEntity entity) {    this.m_entity = entity;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_entity!=null) {
			jsonMap.put(ENTITY, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
		}
	}
}
