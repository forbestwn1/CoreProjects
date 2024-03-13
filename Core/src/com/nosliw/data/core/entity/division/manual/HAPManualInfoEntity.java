package com.nosliw.data.core.entity.division.manual;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.entity.HAPIdEntityType;

public class HAPManualInfoEntity extends HAPEntityInfoImp implements HAPManualWithEntity{

	public static final String INFO = "info";

	private HAPManualEntity m_entity;

	public HAPManualInfoEntity() {	}

	@Override
	public HAPManualEntity getEntity() {    return this.m_entity;    }
	public void setEntity(HAPManualEntity entity) {    
		this.m_entity = entity;     
	}

	@Override
	public HAPIdEntityType getEntityTypeId() {   return this.getEntity().getEntityTypeId();   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITY, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
	}

}
