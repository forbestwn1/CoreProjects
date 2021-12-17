package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;

public class HAPDomainExecutableEntity {

	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;

	//processed attachment
	
	
	//all entity 
	private Map<HAPIdEntityInDomain, HAPExecutableEntityComplex> m_executableEntity;
	
	//id generator
	private HAPGeneratorId m_idGenerator;
	
	public HAPDomainExecutableEntity(HAPGeneratorId idGenerator, HAPDomainValueStructure valueStructureDomain) {
		this.m_idGenerator = idGenerator;
		this.m_valueStructureDomain = valueStructureDomain;
	}
	
	public HAPIdEntityInDomain addExecutableEntity(HAPExecutableEntityComplex executableEntity) {
		HAPIdEntityInDomain entityId = new HAPIdEntityInDomain(this.m_idGenerator.generateId(), executableEntity.getEntityType());
		this.m_executableEntity.put(entityId, executableEntity);
		return entityId;
	}
}
