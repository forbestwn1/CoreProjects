package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPInfoEntityInDomainExecutable extends HAPExecutableImp implements HAPInfoEntityInDomain{

	@HAPAttribute
	public static final String ENTITYID = "entityId";
	
	@HAPAttribute
	public static final String ENTITY = "entity";

	@HAPAttribute
	public static final String EXTERNALCOMPLEXENTITYID = "externalComplexEntityId";

	@HAPAttribute
	public static final String EXTRA = "extra";
	
	//entity id
	private HAPIdEntityInDomain m_entityId;

	//entity
	private HAPExecutableEntityComplex m_entity;

	//when refer to external complex entity
	private String m_externalComplexEntityId;
	
	//extra info for this entity
	private HAPExtraInfoEntityInDomainExecutable m_extraInfo;
	
	public HAPInfoEntityInDomainExecutable(HAPExecutableEntityComplex entity, HAPIdEntityInDomain entityId, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		this.m_entity = entity;
		this.m_entityId = entityId;
		this.m_extraInfo = extraInfo;
	}
	
	public HAPInfoEntityInDomainExecutable(String externalComplexEntityId, HAPIdEntityInDomain entityId, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		this.m_entityId = entityId;
		this.m_externalComplexEntityId = externalComplexEntityId;
		this.m_extraInfo = extraInfo;
	}
	
	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;    }
	
	public HAPExecutableEntityComplex getEntity() {   return this.m_entity;     }
	
	public String getExternalComplexEntityId() {     return this.m_externalComplexEntityId;       }
	
	public HAPExtraInfoEntityInDomainExecutable getExtraInfo() {    return this.m_extraInfo;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITYID, this.m_entityId.toStringValue(HAPSerializationFormat.LITERATE));
		jsonMap.put(EXTRA, this.m_extraInfo.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_entity!=null)  jsonMap.put(ENTITY, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EXTERNALCOMPLEXENTITYID, this.m_externalComplexEntityId);
	}

	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ENTITYID, this.m_entityId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EXTRA, this.m_extraInfo.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_entity!=null)  jsonMap.put(ENTITY, this.m_entity.toExpandedJsonString((HAPDomainEntityExecutableResourceComplex)entityDomain));
		jsonMap.put(EXTERNALCOMPLEXENTITYID, this.m_externalComplexEntityId);
		return HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap);
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(this.m_entity!=null)  jsonMap.put(ENTITY, this.m_entity.toResourceData(runtimeInfo).toString());
	}
	
	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(super.getResourceDependency(runtimeInfo, resourceManager));
		this.buildResourceDependencyForExecutable(out, this.m_entity, runtimeInfo, resourceManager);
		return out;
	}

}
