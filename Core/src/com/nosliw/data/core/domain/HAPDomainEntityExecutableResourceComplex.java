package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPDomainEntityExecutableResourceComplex extends HAPExecutableImp implements HAPDomainEntity{

	@HAPAttribute
	public static String VALUESTRUCTUREDOMAIN = "valueStructureDomain";
	@HAPAttribute
	public static String COMPLEXENTITY = "complexEntity";

	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;

	//all executable entity 
	private Map<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable> m_executableEntity;
	
	//id generator
	private HAPGeneratorId m_idGenerator;
	
	public HAPDomainEntityExecutableResourceComplex() {
		this.m_idGenerator = new HAPGeneratorId();
		this.m_valueStructureDomain = new HAPDomainValueStructure();
		this.m_executableEntity = new LinkedHashMap<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable>();
	}

	public HAPDomainValueStructure getValueStructureDomain() {    return this.m_valueStructureDomain;     }
	
	public HAPIdEntityInDomain addExecutableEntity(HAPExecutableEntityComplex executableEntity, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain entityId = new HAPIdEntityInDomain(this.m_idGenerator.generateId(), executableEntity.getEntityType());
		HAPInfoEntityInDomainExecutable entityInfo = new HAPInfoEntityInDomainExecutable(executableEntity, entityId, extraInfo);
		this.m_executableEntity.put(entityId, entityInfo);
		return entityId;
	}

	public HAPIdEntityInDomain addExecutableEntity(HAPIdComplexEntityInGlobal complexEntityIdInGloabal, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain entityId = new HAPIdEntityInDomain(this.m_idGenerator.generateId(), complexEntityIdInGloabal.getRootResourceId().getResourceType());
		HAPInfoEntityInDomainExecutable entityInfo = new HAPInfoEntityInDomainExecutable(complexEntityIdInGloabal, entityId, extraInfo);
		this.m_executableEntity.put(entityId, entityInfo);
		return entityId;
	}

	@Override
	public HAPInfoEntityInDomain getEntityInfo(HAPIdEntityInDomain entityId) {    return this.getEntityInfoExecutable(entityId);     }
	public HAPInfoEntityInDomainExecutable getEntityInfoExecutable(HAPIdEntityInDomain entityId) {	return this.m_executableEntity.get(entityId);	}

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTUREDOMAIN, HAPJsonUtility.buildJson(this.m_valueStructureDomain, HAPSerializationFormat.JSON));
		
		Map<String, String> entityJsonObj = new LinkedHashMap<String, String>();
		for(HAPIdEntityInDomain entityId : this.m_executableEntity.keySet()) {
			entityJsonObj.put(entityId.toStringValue(HAPSerializationFormat.LITERATE), this.m_executableEntity.get(entityId).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(COMPLEXENTITY, HAPJsonUtility.buildMapJson(entityJsonObj));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);

		Map<String, String> entityJsonObj = new LinkedHashMap<String, String>();
		for(HAPIdEntityInDomain entityId : this.m_executableEntity.keySet()) {
			entityJsonObj.put(entityId.toStringValue(HAPSerializationFormat.LITERATE), this.m_executableEntity.get(entityId).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(COMPLEXENTITY, HAPJsonUtility.buildMapJson(entityJsonObj));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(super.getResourceDependency(runtimeInfo, resourceManager));
		for(HAPIdEntityInDomain entityId : this.m_executableEntity.keySet()) {
			this.buildResourceDependencyForExecutable(out, this.m_executableEntity.get(entityId), runtimeInfo, resourceManager);
		}
		return out;
	}
}
