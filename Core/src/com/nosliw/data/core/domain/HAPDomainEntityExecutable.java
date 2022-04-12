package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;

public class HAPDomainEntityExecutable extends HAPSerializableImp implements HAPDomainEntity{

	private static String VALUESTRUCTUREDOMAIN = "valueStructureDomain";
	private static String ATTACHMENTDOMAIN = "attachmentDomain";
	private static String COMPLEXENTITY = "complexEntity";
	private static String MAINENTITYID = "mainEntityId";
	
	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;

	//all entity 
	private Map<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable> m_executableEntity;
	
	//main entity
	private HAPIdEntityInDomain m_mainComplexEntityId;

	//main entity
	private HAPIdEntityInDomain m_rootComplexEntityId;

	//id generator
	private HAPGeneratorId m_idGenerator;
	
	public HAPDomainEntityExecutable(HAPGeneratorId idGenerator) {
		this.m_idGenerator = idGenerator;
		this.m_valueStructureDomain = new HAPDomainValueStructure(this.m_idGenerator);
		this.m_executableEntity = new LinkedHashMap<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable>();
	}

	public HAPDomainValueStructure getValueStructureDomain() {    return this.m_valueStructureDomain;     }
	
	public HAPIdEntityInDomain getMainEntityId() {   return this.m_mainComplexEntityId;   }
	public void setMainEntityId(HAPIdEntityInDomain mainEntityId) {    this.m_mainComplexEntityId = mainEntityId;      }
	
	public HAPIdEntityInDomain getRootEntityId() {   return this.m_rootComplexEntityId;   }
	public void setRootEntityId(HAPIdEntityInDomain rootEntityId) {    this.m_rootComplexEntityId = rootEntityId;      }
	
	public HAPIdEntityInDomain addExecutableEntity(HAPExecutableEntityComplex executableEntity) {
		HAPIdEntityInDomain entityId = new HAPIdEntityInDomain(this.m_idGenerator.generateId(), executableEntity.getEntityType());
		HAPInfoEntityInDomainExecutable entityInfo = new HAPInfoEntityInDomainExecutable(executableEntity, entityId);
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
		jsonMap.put(ATTACHMENTDOMAIN, this.m_attachmentDomain.toString());
		jsonMap.put(MAINENTITYID, this.m_mainComplexEntityId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(COMPLEXENTITY, this.getEntityInfoExecutable(m_mainComplexEntityId).toExpandedJsonString(this));
		
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
}
