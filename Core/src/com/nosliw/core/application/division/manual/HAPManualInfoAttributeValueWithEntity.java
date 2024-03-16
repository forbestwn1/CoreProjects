package com.nosliw.core.application.division.manual;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPIdBrickType;

public class HAPManualInfoAttributeValueWithEntity extends HAPManualInfoAttributeValue implements HAPManualWithEntity{

	private HAPManualEntity m_entity;
	
	private HAPIdBrickType m_entityTypeId;

	public HAPManualInfoAttributeValueWithEntity(String valueType, HAPIdBrickType entityTypeId) {
		super(valueType);
		this.m_entityTypeId = entityTypeId;
	}
	
	@Override
	public HAPManualEntity getEntity() {	return this.m_entity;	}
	public void setEntity(HAPManualEntity entity) {    this.m_entity = entity;    }
	
	@Override
	public HAPIdBrickType getEntityTypeId() {   return this.m_entityTypeId;    }
	public void setEntityTypeId(HAPIdBrickType entityTypeId) {    this.m_entityTypeId = entityTypeId;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_entity!=null) {
			jsonMap.put(ENTITY, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ENTITYTYPEID, this.m_entityTypeId.toStringValue(HAPSerializationFormat.JSON));
	}
}
