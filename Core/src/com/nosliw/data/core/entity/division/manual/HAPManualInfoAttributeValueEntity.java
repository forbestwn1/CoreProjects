package com.nosliw.data.core.entity.division.manual;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.HAPInfoEntityType;

public class HAPManualInfoAttributeValueEntity extends HAPManualInfoAttributeValue{

	//entity definition
	public static final String ENTITY = "entity";

	private HAPManualEntity m_entity;
	
	public HAPManualInfoAttributeValueEntity(HAPManualEntity entity) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_ENTITY, new HAPInfoEntityType(entity.getEntityTypeId()));
		this.m_entity = entity;
	}

	public HAPManualEntity getEntity() {    return this.m_entity;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
	}
}
