package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

//all information for complex resource 
public class HAPPackageComplexResource {

	//entity domain definition
	private HAPDomainEntityDefinitionGlobal m_definitionDomain;
	
	//entity domain executable
	private HAPDomainEntityExecutableResourceComplex m_executableDomain;

	//mapping between complex entity definition id to executable id 
	private Map<HAPIdEntityInDomain, HAPIdEntityInDomain> m_executableComplexEntityIdByDefinitionComplexEntityId;
	private Map<HAPIdEntityInDomain, HAPIdEntityInDomain> m_definitionComplexEntityIdByExecutableComplexEntityId;
	
	private HAPDomainAttachment m_attachmentDomain;
	
	//id generator
	private HAPGeneratorId m_idGenerator;

	public HAPPackageComplexResource(HAPDomainEntityDefinitionGlobal definitionDomain, HAPGeneratorId idGenerator) {
		this.m_definitionDomain = definitionDomain;
		this.m_idGenerator = idGenerator;
		this.m_executableDomain = new HAPDomainEntityExecutableResourceComplex(this.m_idGenerator);
		this.m_attachmentDomain = new HAPDomainAttachment(this.m_idGenerator);
		this.m_executableComplexEntityIdByDefinitionComplexEntityId = new LinkedHashMap<HAPIdEntityInDomain, HAPIdEntityInDomain>();
		this.m_definitionComplexEntityIdByExecutableComplexEntityId = new LinkedHashMap<HAPIdEntityInDomain, HAPIdEntityInDomain>();
	}
	
	public HAPResourceIdSimple getRootResourceId() {    return this.m_definitionDomain.getRootResourceId();     }
	
	public HAPDomainEntityDefinitionGlobal getDefinitionDomain() {		return this.m_definitionDomain; 	}
	
	public HAPDomainEntityExecutableResourceComplex getExecutableDomain() {    return this.m_executableDomain;     }
	
	public HAPDomainAttachment getAttachmentDomain() {   return this.m_attachmentDomain;    }
	
	public HAPIdEntityInDomain getExecutableEntityIdByDefinitionEntityId(HAPIdEntityInDomain defEntityId) {   return this.m_executableComplexEntityIdByDefinitionComplexEntityId.get(defEntityId);  	}
	public HAPIdEntityInDomain getDefinitionEntityIdByExecutableEntityId(HAPIdEntityInDomain defEntityId) {   return this.m_executableComplexEntityIdByDefinitionComplexEntityId.get(defEntityId);  	}
	
	
	public HAPIdEntityInDomain addExecutableEntity(HAPIdEntityInDomain definitionEntityId, HAPExecutableEntityComplex executableEntity, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain out = this.m_executableDomain.addExecutableEntity(executableEntity, extraInfo);
		this.m_executableComplexEntityIdByDefinitionComplexEntityId.put(out, extraInfo.getEntityDefinitionId());
		this.m_definitionComplexEntityIdByExecutableComplexEntityId.put(extraInfo.getEntityDefinitionId(), out);
		return out;
	}

	public HAPIdEntityInDomain addExecutableEntity(HAPIdEntityInDomain definitionEntityId, HAPInfoResourceIdNormalize normalizedResourceId, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain out = this.m_executableDomain.addExecutableEntity(normalizedResourceId, extraInfo);
		this.m_executableComplexEntityIdByDefinitionComplexEntityId.put(out, extraInfo.getEntityDefinitionId());
		this.m_definitionComplexEntityIdByExecutableComplexEntityId.put(extraInfo.getEntityDefinitionId(), out);
		return out;
	}

}
