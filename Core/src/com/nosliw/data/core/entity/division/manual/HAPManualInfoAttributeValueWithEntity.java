package com.nosliw.data.core.entity.division.manual;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.entity.HAPIdEntityType;

public class HAPManualInfoAttributeValueWithEntity extends HAPManualInfoAttributeValue implements HAPManualWithEntity{

	private HAPManualEntity m_entity;
	
	private HAPIdEntityType m_entityTypeId;

	public HAPManualInfoAttributeValueWithEntity(String valueType, HAPIdEntityType entityTypeId) {
		super(valueType);
		this.m_entityTypeId = entityTypeId;
	}
	
	@Override
	public HAPManualEntity getEntity() {	return this.m_entity;	}
	public void setEntity(HAPManualEntity entity) {    this.m_entity = entity;    }
	
	@Override
	public HAPIdEntityType getEntityTypeId() {   return this.m_entityTypeId;    }
	public void setEntityTypeId(HAPIdEntityType entityTypeId) {    this.m_entityTypeId = entityTypeId;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_entity!=null) {
			jsonMap.put(ENTITY, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ENTITYTYPEID, this.m_entityTypeId.toStringValue(HAPSerializationFormat.JSON));
	}
}
