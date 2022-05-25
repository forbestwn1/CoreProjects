package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;

public class HAPInfoEntityInDomainExecutable extends HAPSerializableImp implements HAPInfoEntityInDomain{

	public static final String ENTITYID = "entityId";
	
	public static final String ENTITY = "entity";

	public static final String EXTERNALRESOURCE = "externalResource";

	public static final String EXTRA = "extra";
	
	//entity id
	private HAPIdEntityInDomain m_entityId;

	//entity
	private HAPExecutableEntityComplex m_entity;

	//when refer to external complex entity
	private HAPInfoResourceIdNormalize m_externalComplexResourceInfo;
	
	//extra info for this entity
	private HAPExtraInfoEntityInDomainExecutable m_extraInfo;
	
	public HAPInfoEntityInDomainExecutable(HAPExecutableEntityComplex entity, HAPIdEntityInDomain entityId, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		this.m_entity = entity;
		this.m_entityId = entityId;
		this.m_extraInfo = extraInfo;
	}
	
	public HAPInfoEntityInDomainExecutable(HAPInfoResourceIdNormalize externalComplexResourceInfo, HAPIdEntityInDomain entityId, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		this.m_entityId = entityId;
		this.m_externalComplexResourceInfo = externalComplexResourceInfo;
		this.m_extraInfo = extraInfo;
	}
	
	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;    }
	
	public HAPExecutableEntityComplex getEntity() {   return this.m_entity;     }
	
	public HAPInfoResourceIdNormalize getExternalComplexResourceInfo() {     return this.m_externalComplexResourceInfo;       }
	
	public HAPExtraInfoEntityInDomainExecutable getExtraInfo() {    return this.m_extraInfo;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITYID, this.m_entityId.toStringValue(HAPSerializationFormat.LITERATE));
		jsonMap.put(EXTRA, this.m_extraInfo.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_entity!=null)  jsonMap.put(ENTITY, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_externalComplexResourceInfo!=null)  jsonMap.put(EXTERNALRESOURCE, this.m_externalComplexResourceInfo.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITYID, this.m_entityId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EXTRA, this.m_extraInfo.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_entity!=null)  jsonMap.put(ENTITY, this.m_entity.toExpandedJsonString((HAPDomainEntityExecutableResourceComplex)entityDomain));
		if(this.m_externalComplexResourceInfo!=null)  jsonMap.put(EXTERNALRESOURCE, this.m_externalComplexResourceInfo.toStringValue(HAPSerializationFormat.JSON));
		return HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap);
	}

}
