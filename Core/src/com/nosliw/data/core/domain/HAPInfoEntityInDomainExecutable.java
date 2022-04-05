package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;

public class HAPInfoEntityInDomainExecutable extends HAPSerializableImp implements HAPInfoEntityInDomain{

	public static final String ENTITYID = "entityId";
	
	public static final String ENTITY = "entity";
	
	//entity id
	private HAPIdEntityInDomain m_entityId;

	private HAPExecutableEntityComplex m_entity;

	public HAPInfoEntityInDomainExecutable(HAPExecutableEntityComplex entity, HAPIdEntityInDomain entityId) {
		this.m_entity = entity;
		this.m_entityId = entityId;
	}
	
	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;    }
	
	public HAPExecutableEntityComplex getEntity() {   return this.m_entity;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTITYID, this.m_entityId.toStringValue(HAPSerializationFormat.LITERATE));
		jsonMap.put(ENTITY, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		// TODO Auto-generated method stub
		return null;
	}

}
