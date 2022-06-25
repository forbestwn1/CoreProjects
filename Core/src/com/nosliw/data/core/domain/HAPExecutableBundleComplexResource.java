package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

//all information for complex resource 
@HAPEntityWithAttribute
public class HAPExecutableBundleComplexResource extends HAPExecutableImp{

	@HAPAttribute
	public static String EXECUTABLEENTITYDOMAIN = "executableDomain";

	//root resource
	private HAPResourceIdSimple m_rootResourceId;
	
	//entity domain definition
	private HAPDomainEntityDefinitionGlobal m_definitionEntityDomain;
	
	//entity domain executable
	private HAPDomainEntityExecutableResourceComplex m_executableEntityDomain;

	//mapping between complex entity definition id to executable id 
	private Map<HAPIdEntityInDomain, HAPIdEntityInDomain> m_executableComplexEntityIdByDefinitionComplexEntityId;
	private Map<HAPIdEntityInDomain, HAPIdEntityInDomain> m_definitionComplexEntityIdByExecutableComplexEntityId;
	
	//all other complex resource this resource depend on
	private Set<HAPResourceIdSimple> m_complexResourceDependency;
	
	private HAPDomainAttachment m_attachmentDomain;
	
	public HAPExecutableBundleComplexResource(HAPResourceIdSimple rootResourceId, HAPDomainEntityDefinitionGlobal definitionDomain) {
		this.m_rootResourceId = rootResourceId;
		this.m_definitionEntityDomain = definitionDomain;
		this.m_executableEntityDomain = new HAPDomainEntityExecutableResourceComplex();
		this.m_attachmentDomain = new HAPDomainAttachment();
		this.m_executableComplexEntityIdByDefinitionComplexEntityId = new LinkedHashMap<HAPIdEntityInDomain, HAPIdEntityInDomain>();
		this.m_definitionComplexEntityIdByExecutableComplexEntityId = new LinkedHashMap<HAPIdEntityInDomain, HAPIdEntityInDomain>();
		this.m_complexResourceDependency = new HashSet<HAPResourceIdSimple>();
	}
	
	public HAPResourceIdSimple getRootResourceId() {    return this.m_rootResourceId;    }
	
	public HAPIdEntityInDomain getDefinitionRootEntityId() {	return this.m_definitionEntityDomain.getResourceDefinitionByResourceId(this.m_rootResourceId).getEntityId();  }
	public HAPIdEntityInDomain getExecutableRootEntityId() {    return this.getExecutableEntityIdByDefinitionEntityId(this.getDefinitionRootEntityId());   }

	public HAPDomainEntityDefinitionGlobal getDefinitionDomain() {		return this.m_definitionEntityDomain; 	}
	
	public HAPDomainEntityExecutableResourceComplex getExecutableDomain() {    return this.m_executableEntityDomain;     }
	
	public HAPDomainValueStructure getValueStructureDomain() {    return this.m_executableEntityDomain.getValueStructureDomain();    }
	
	public HAPDomainAttachment getAttachmentDomain() {   return this.m_attachmentDomain;    }
	
	public HAPIdEntityInDomain getExecutableEntityIdByDefinitionEntityId(HAPIdEntityInDomain defEntityId) {   return this.m_executableComplexEntityIdByDefinitionComplexEntityId.get(defEntityId);  	}
	public HAPIdEntityInDomain getDefinitionEntityIdByExecutableEntityId(HAPIdEntityInDomain defEntityId) {   return this.m_definitionComplexEntityIdByExecutableComplexEntityId.get(defEntityId);  	}
	
	public void addComplexResourceDependency(HAPResourceIdSimple resourceId) {   this.m_complexResourceDependency.add(resourceId);    }
	public Set<HAPResourceIdSimple> getComplexResourceDependency(){    return this.m_complexResourceDependency;     }
	
	public HAPInfoEntityInDomainExecutable getEntityInfoExecutable(HAPInfoResourceIdNormalize normalizedResourceInfo) {
		HAPIdEntityInDomain entityId = this.m_definitionEntityDomain.getResourceDomainBySimpleResourceId(normalizedResourceInfo.getRootResourceIdSimple()).getRootEntityId();
		HAPIdEntityInDomain outEntityDefId = HAPUtilityDomain.getEntityDescent(entityId, normalizedResourceInfo.getPath(), this.m_definitionEntityDomain);
		HAPIdEntityInDomain outEntityExeId = this.getExecutableEntityIdByDefinitionEntityId(outEntityDefId);
		return this.m_executableEntityDomain.getEntityInfoExecutable(outEntityExeId);
	}

	public HAPIdEntityInDomain addExecutableEntity(HAPIdEntityInDomain definitionEntityId, HAPExecutableEntityComplex executableEntity, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain out = this.m_executableEntityDomain.addExecutableEntity(executableEntity, extraInfo);
		this.m_executableComplexEntityIdByDefinitionComplexEntityId.put(extraInfo.getEntityDefinitionId(), out);
		this.m_definitionComplexEntityIdByExecutableComplexEntityId.put(out, extraInfo.getEntityDefinitionId());
		return out;
	}

	public HAPIdEntityInDomain addExecutableEntity(HAPIdEntityInDomain definitionEntityId, HAPIdComplexEntityInGlobal complexEntityIdInGloabal, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain out = this.m_executableEntityDomain.addExecutableEntity(complexEntityIdInGloabal, extraInfo);
		this.m_executableComplexEntityIdByDefinitionComplexEntityId.put(extraInfo.getEntityDefinitionId(), out);
		this.m_definitionComplexEntityIdByExecutableComplexEntityId.put(out, extraInfo.getEntityDefinitionId());
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXECUTABLEENTITYDOMAIN, HAPJsonUtility.buildJson(this.m_executableEntityDomain, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(EXECUTABLEENTITYDOMAIN, this.m_executableEntityDomain.toResourceData(runtimeInfo).toString());
	}
	
	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(super.getResourceDependency(runtimeInfo, resourceManager));
		this.buildResourceDependencyForExecutable(out, m_executableEntityDomain, runtimeInfo, resourceManager);
		return out;
	}
}
