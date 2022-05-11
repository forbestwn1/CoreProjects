package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

//entity domain for resource for complex entity
public class HAPDomainEntityDefinitionSimpleResourceComplex extends HAPDomainEntityDefinitionSimpleResource{

	//parent info for entity id
	private Map<HAPIdEntityInDomain, HAPInfoParentComplex> m_parentComplexInfo;
	
	public HAPDomainEntityDefinitionSimpleResourceComplex(HAPResourceIdSimple resourceId, HAPGeneratorId idGenerator, HAPManagerDomainEntityDefinition entityDefMan) {
		super(resourceId, idGenerator, entityDefMan);
		this.m_parentComplexInfo = new LinkedHashMap<HAPIdEntityInDomain, HAPInfoParentComplex>();
	}

	@Override
	public boolean isForComplexEntity() {  return true;    }

	public void buildComplexParentRelation(HAPIdEntityInDomain entityId, HAPInfoParentComplex parentInfo) {		this.m_parentComplexInfo.put(entityId, parentInfo); 	}
	public HAPInfoParentComplex getParentInfo(HAPIdEntityInDomain entityId) {    return this.m_parentComplexInfo.get(entityId);     }
	

}
