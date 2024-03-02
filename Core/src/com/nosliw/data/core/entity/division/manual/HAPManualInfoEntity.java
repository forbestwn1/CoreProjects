package com.nosliw.data.core.entity.division.manual;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPManualInfoEntity extends HAPEntityInfoImp{

	public static final String ENTITYTYPE = "entityType";

	public static final String INFO = "info";

	//entity definition
	public static final String ENTITY = "entity";

	private HAPManualEntity m_entity;
	
	public HAPManualInfoEntity() {	}

	public HAPManualEntity getEntity() {    return this.m_entity;    }
	public void setEntity(HAPManualEntity entity) {    this.m_entity = entity;     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
	}
}
