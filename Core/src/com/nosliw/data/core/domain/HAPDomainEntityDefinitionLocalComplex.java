package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.definition.HAPManagerDomainEntityDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

//entity domain for resource for complex entity
//for all complex entity in this domain, there are parent and child relation between two complex entity and setting for this relation   
public class HAPDomainEntityDefinitionLocalComplex extends HAPDomainEntityDefinitionLocal{

	//parent info for entity id
	private Map<HAPIdEntityInDomain, HAPInfoParentComplex> m_parentComplexInfo;
	
	public HAPDomainEntityDefinitionLocalComplex(HAPResourceIdSimple resourceId, HAPGeneratorId idGenerator, HAPManagerDomainEntityDefinition entityDefMan) {
		super(resourceId, idGenerator, entityDefMan);
		this.m_parentComplexInfo = new LinkedHashMap<HAPIdEntityInDomain, HAPInfoParentComplex>();
	}

	@Override
	public boolean isForComplexEntity() {  return true;    }

	public void buildComplexParentRelation(HAPIdEntityInDomain entityId, HAPInfoParentComplex parentInfo) {		this.m_parentComplexInfo.put(entityId, parentInfo); 	}
	public HAPInfoParentComplex getParentInfo(HAPIdEntityInDomain entityId) {    return this.m_parentComplexInfo.get(entityId);     }

}
