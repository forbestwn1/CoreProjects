package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;

public class HAPDomainEntityExecutable extends HAPSerializableImp implements HAPDomainEntity{

	private static String VALUESTRUCTUREDOMAIN = "valueStructureDomain";
	private static String ATTACHMENTDOMAIN = "attachmentDomain";
	private static String COMPLEXENTITY = "complexEntity";
	private static String MAINENTITYID = "mainEntityId";
	
	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;

	//processed attachment
	private HAPDomainAttachment m_attachmentDomain;
	
	//all entity 
	private Map<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable> m_executableEntity;
	
	//main entity
	private HAPIdEntityInDomain m_mainComplexEntityId;

	//id generator
	private HAPGeneratorId m_idGenerator;
	
	public HAPDomainEntityExecutable(HAPGeneratorId idGenerator) {
		this.m_idGenerator = idGenerator;
		this.m_valueStructureDomain = new HAPDomainValueStructure(this.m_idGenerator);
		this.m_attachmentDomain = new HAPDomainAttachment();
		this.m_executableEntity = new LinkedHashMap<HAPIdEntityInDomain, HAPInfoEntityInDomainExecutable>();
	}

	public HAPDomainValueStructure getValueStructureDomain() {    return this.m_valueStructureDomain;     }
	
	public HAPDomainAttachment getAttachmentDomain() {   return this.m_attachmentDomain;     }

	public void setMainEntityId(HAPIdEntityInDomain mainEntityId) {    this.m_mainComplexEntityId = mainEntityId;      }
	
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
		
		List<String> entityJsonList = new ArrayList<String>();
		for(HAPInfoEntityInDomainExecutable entityInfo : this.m_executableEntity.values()) {
			entityJsonList.add(entityInfo.toExpandedJsonString(this));
		}
		jsonMap.put(COMPLEXENTITY, HAPJsonUtility.buildArrayJson(entityJsonList.toArray(new String[0])));
		
		return HAPJsonUtility.buildMapJson(jsonMap);
	}
}
