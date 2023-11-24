package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
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
//	@HAPAttribute
//	public static String COMPLEXENTITY = "complexEntity";
	@HAPAttribute
	public static String ROOTENTITY = "rootEntity";
	@HAPAttribute
	public static String EXTERNALENTITY = "externalEntity";

	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;

	//all other complex resource this resource depend on, external dependency
	private Map<String, HAPIdComplexEntityInGlobal> m_externalComplexEntityDpendency;

	//id generator
	private HAPGeneratorId m_idGenerator;

	private HAPExecutableEntityComplex m_rootEntity;
	
	//all executable entity 
//	private Map<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable> m_executableEntity;
	
	
	public HAPDomainEntityExecutableResourceComplex() {
		this.m_idGenerator = new HAPGeneratorId();
		this.m_valueStructureDomain = new HAPDomainValueStructure();
		this.m_externalComplexEntityDpendency = new LinkedHashMap<String, HAPIdComplexEntityInGlobal>();
	}

	public void setRootEntity(HAPExecutableEntityComplex rootEntity) {   this.m_rootEntity = rootEntity;     }
	public HAPExecutableEntityComplex getRootEntity() {    return this.m_rootEntity;     }
	
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
//	public HAPInfoEntityInDomainExecutable getEntityInfoExecutable(HAPIdEntityInDomain entityId) {	return this.m_executableEntity.get(entityId);	}

	public HAPIdComplexEntityInGlobal getExternalEntityGlobalId(HAPIdEntityInDomain entityId) {
		return this.m_externalComplexEntityDpendency.get(this.getEntityInfoExecutable(entityId).getExternalComplexEntityId());
	}
	
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
		jsonMap.put(ROOTENTITY, this.m_rootEntity.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EXTERNALENTITY, HAPUtilityJson.buildJson(this.m_externalComplexEntityDpendency, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(ROOTENTITY, this.m_rootEntity.toResourceData(runtimeInfo).toString());
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(super.getResourceDependency(runtimeInfo, resourceManager));
		this.buildResourceDependencyForExecutable(out, this.m_rootEntity, runtimeInfo, resourceManager);
		return out;
	}
}
