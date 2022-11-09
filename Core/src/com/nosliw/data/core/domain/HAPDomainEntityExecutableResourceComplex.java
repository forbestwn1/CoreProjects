package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPDomainEntityExecutableResourceComplex extends HAPExecutableImp implements HAPDomainEntity{

	@HAPAttribute
	public static String VALUESTRUCTUREDOMAIN = "valueStructureDomain";
	@HAPAttribute
	public static String COMPLEXENTITY = "complexEntity";
	@HAPAttribute
	public static String EXTERNALENTITY = "externalEntity";

	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;

	//all executable entity 
	private Map<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable> m_executableEntity;
	
	//all other complex resource this resource depend on, external dependency
	private Map<String, HAPIdComplexEntityInGlobal> m_externalComplexEntityDpendency;
	
	//id generator
	private HAPGeneratorId m_idGenerator;
	
	public HAPDomainEntityExecutableResourceComplex() {
		this.m_idGenerator = new HAPGeneratorId();
		this.m_valueStructureDomain = new HAPDomainValueStructure();
		this.m_executableEntity = new LinkedHashMap<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable>();
		this.m_externalComplexEntityDpendency = new LinkedHashMap<String, HAPIdComplexEntityInGlobal>();
	}

	public HAPDomainValueStructure getValueStructureDomain() {    return this.m_valueStructureDomain;     }
	
	public HAPIdEntityInDomain addExecutableEntity(HAPExecutableEntityComplex executableEntity, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain entityId = new HAPIdEntityInDomain(this.m_idGenerator.generateId(), executableEntity.getEntityType());
		HAPInfoEntityInDomainExecutable entityInfo = new HAPInfoEntityInDomainExecutable(executableEntity, entityId, extraInfo);
		this.m_executableEntity.put(entityId, entityInfo);
		return entityId;
	}

	
	public HAPIdEntityInDomain addExecutableEntity(HAPIdComplexEntityInGlobal complexEntityIdInGloabal, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		String id = this.m_idGenerator.generateId();
		this.m_externalComplexEntityDpendency.put(id, complexEntityIdInGloabal);
		HAPIdEntityInDomain entityId = new HAPIdEntityInDomain(id, complexEntityIdInGloabal.getResourceInfo().getResourceEntityType());
		HAPInfoEntityInDomainExecutable entityInfo = new HAPInfoEntityInDomainExecutable(id, entityId, extraInfo);
		this.m_executableEntity.put(entityId, entityInfo);
		return entityId;
	}

	@Override
	public HAPInfoEntityInDomain getEntityInfo(HAPIdEntityInDomain entityId) {    return this.getEntityInfoExecutable(entityId);     }
	public HAPInfoEntityInDomainExecutable getEntityInfoExecutable(HAPIdEntityInDomain entityId) {	return this.m_executableEntity.get(entityId);	}

	public Set<HAPResourceIdSimple> getComplexResourceDependency(){
		Set<HAPResourceIdSimple> out = new HashSet<HAPResourceIdSimple>();
		for(String id : this.m_externalComplexEntityDpendency.keySet()) {
			out.add(this.m_externalComplexEntityDpendency.get(id).getResourceInfo().getRootResourceIdSimple());
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTUREDOMAIN, HAPUtilityJson.buildJson(this.m_valueStructureDomain, HAPSerializationFormat.JSON));
		
		Map<String, String> entityJsonObj = new LinkedHashMap<String, String>();
		for(HAPIdEntityInDomain entityId : this.m_executableEntity.keySet()) {
			entityJsonObj.put(entityId.toStringValue(HAPSerializationFormat.LITERATE), this.m_executableEntity.get(entityId).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(COMPLEXENTITY, HAPUtilityJson.buildMapJson(entityJsonObj));
		
		jsonMap.put(EXTERNALENTITY, HAPUtilityJson.buildJson(this.m_externalComplexEntityDpendency, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);

		Map<String, String> entityJsonObj = new LinkedHashMap<String, String>();
		for(HAPIdEntityInDomain entityId : this.m_executableEntity.keySet()) {
			entityJsonObj.put(entityId.toStringValue(HAPSerializationFormat.LITERATE), this.m_executableEntity.get(entityId).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(COMPLEXENTITY, HAPUtilityJson.buildMapJson(entityJsonObj));
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
