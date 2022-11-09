package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;

public class HAPDomainEntityExecutableResourceComplex2 extends HAPSerializableImp implements HAPDomainEntity{

	private static String VALUESTRUCTUREDOMAIN = "valueStructureDomain";
	private static String COMPLEXENTITY = "complexEntity";
	private static String ROOTENTITYID = "rootEntityId";

	//domain id for this domain
	private String m_domainId;
	
	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;

	//all entity 
	private Map<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable> m_executableEntity;
	
	//main entity
	private HAPIdEntityInDomain m_rootComplexEntityId;

	//id generator
	private HAPGeneratorId m_idGenerator;
	
	public HAPDomainEntityExecutableResourceComplex2(String domainId, HAPGeneratorId idGenerator) {
		this.m_domainId = domainId;
		this.m_idGenerator = idGenerator;
		this.m_valueStructureDomain = new HAPDomainValueStructure(this.m_idGenerator);
		this.m_executableEntity = new LinkedHashMap<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable>();
	}

	public String getDomainId() {    return this.m_domainId;     }
	
	public HAPDomainValueStructure getValueStructureDomain() {    return this.m_valueStructureDomain;     }
	
	public HAPIdEntityInDomain getRootEntityId() {   return this.m_rootComplexEntityId;   }
	public void setRootEntityId(HAPIdEntityInDomain rootEntityId) {    this.m_rootComplexEntityId = rootEntityId;      }
	
	public HAPIdEntityInDomain addExecutableEntity(HAPExecutableEntityComplex executableEntity, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain entityId = new HAPIdEntityInDomain(this.m_idGenerator.generateId(), executableEntity.getEntityType(), this.m_domainId);
		HAPInfoEntityInDomainExecutable entityInfo = new HAPInfoEntityInDomainExecutable(executableEntity, entityId, extraInfo);
		this.m_executableEntity.put(entityId, entityInfo);
		return entityId;
	}

	public HAPIdEntityInDomain addExecutableEntity(HAPInfoResourceIdNormalize normalizedResourceId, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain entityId = new HAPIdEntityInDomain(this.m_idGenerator.generateId(), normalizedResourceId.getResourceEntityType(), this.m_domainId);
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
		
		return HAPUtilityJson.buildMapJson(jsonMap);
	}
}
