package com.nosliw.data.core.entity.division.manual;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualInfoAttributeValueEntity extends HAPManualInfoAttributeValueWithEntity{

	//entity definition
	public static final String ENTITY = "entity";

	private HAPManualEntity m_entity;
	
	public HAPManualInfoAttributeValueEntity(HAPManualEntity entity) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_ENTITY, entity.getEntityTypeId());
		this.m_entity = entity;
	}

	@Override
	public HAPManualEntity getEntity() {    return this.m_entity;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
	}
}
