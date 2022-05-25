package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;

public class HAPDomainEntityExecutableResourceComplex extends HAPSerializableImp implements HAPDomainEntity{

	private static String VALUESTRUCTUREDOMAIN = "valueStructureDomain";
	private static String COMPLEXENTITY = "complexEntity";
	private static String ROOTENTITYID = "rootEntityId";

	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;

	//all executable entity 
	private Map<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable> m_executableEntity;
	
	//id generator
	private HAPGeneratorId m_idGenerator;
	
	public HAPDomainEntityExecutableResourceComplex(HAPGeneratorId idGenerator) {
		this.m_idGenerator = idGenerator;
		this.m_valueStructureDomain = new HAPDomainValueStructure(this.m_idGenerator);
		this.m_executableEntity = new LinkedHashMap<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable>();
	}

	public HAPDomainValueStructure getValueStructureDomain() {    return this.m_valueStructureDomain;     }
	
	public HAPIdEntityInDomain addExecutableEntity(HAPExecutableEntityComplex executableEntity, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain entityId = new HAPIdEntityInDomain(this.m_idGenerator.generateId(), executableEntity.getEntityType());
		HAPInfoEntityInDomainExecutable entityInfo = new HAPInfoEntityInDomainExecutable(executableEntity, entityId, extraInfo);
		this.m_executableEntity.put(entityId, entityInfo);
		return entityId;
	}

	public HAPIdEntityInDomain addExecutableEntity(HAPInfoResourceIdNormalize normalizedResourceId, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain entityId = new HAPIdEntityInDomain(this.m_idGenerator.generateId(), normalizedResourceId.getResourceEntityType());
		HAPInfoEntityInDomainExecutable entityInfo = new HAPInfoEntityInDomainExecutable(normalizedResourceId, entityId, extraInfo);
		this.m_executableEntity.put(entityId, entityInfo);
		return entityId;
	}

	@Override
	public HAPInfoEntityInDomain getEntityInfo(HAPIdEntityInDomain entityId) {    return this.getEntityInfoExecutable(entityId);     }
	public HAPInfoEntityInDomainExecutable getEntityInfoExecutable(HAPIdEntityInDomain entityId) {	return this.m_executableEntity.get(entityId);	}

	@Override
	public String toString() {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(VALUESTRUCTUREDOMAIN, this.m_valueStructureDomain.toString());
		jsonMap.put(COMPLEXENTITY, this.getEntityInfoExecutable(m_mainComplexEntityId).toExpandedJsonString(this));
		
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
}
