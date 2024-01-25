package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

//bundle is complex resource, it has all information of a complex resource 
@HAPEntityWithAttribute
public class HAPExecutableBundle extends HAPExecutableImp{

	@HAPAttribute
	public static String EXECUTABLEENTITYDOMAIN = "executableDomain";

	//root resource
	private HAPResourceIdSimple m_rootResourceId;
	
	//entity domain definition
	private HAPDomainEntityDefinitionGlobal m_definitionEntityDomain;
	
	//entity domain executable
	private HAPDomainEntityExecutableResourceComplex m_executableEntityDomain;

	//executable entity by id
	private Map<String, String> m_exeEntityPathById;
	
	//
	private HAPDomainAttachment m_attachmentDomain;
	
	public HAPExecutableBundle(HAPResourceIdSimple rootResourceId, HAPDomainEntityDefinitionGlobal definitionDomain) {
		this.m_rootResourceId = rootResourceId;
		this.m_definitionEntityDomain = definitionDomain;
		this.m_executableEntityDomain = new HAPDomainEntityExecutableResourceComplex();
		this.m_attachmentDomain = new HAPDomainAttachment();
		this.m_exeEntityPathById = new LinkedHashMap<String, String>();
	}
	
	public HAPResourceIdSimple getRootResourceId() {    return this.m_rootResourceId;    }
	
	public HAPIdEntityInDomain getDefinitionRootEntityId() {	return this.m_definitionEntityDomain.getResourceDefinitionByResourceId(this.m_rootResourceId).getEntityId();  }

	public HAPDomainEntityDefinitionGlobal getDefinitionDomain() {		return this.m_definitionEntityDomain; 	}
	
	public HAPDomainEntityExecutableResourceComplex getExecutableDomain() {    return this.m_executableEntityDomain;     }
	
	public HAPDomainValueStructure getValueStructureDomain() {    return this.m_executableEntityDomain.getValueStructureDomain();    }
	
	public HAPDomainAttachment getAttachmentDomain() {   return this.m_attachmentDomain;    }
	
	public Set<HAPResourceIdSimple> getComplexResourceDependency(){   return this.m_executableEntityDomain.getComplexResourceDependency();  }
	
	public void setEntityPathById(String id, String path) {
		this.m_exeEntityPathById.put(id, path);
	}
	public HAPExecutableEntity getExecutableEntityById(String id) {
		String path = this.m_exeEntityPathById.get(id);
		return this.m_executableEntityDomain.getRootEntity().getDescendantEntity(new HAPPath(path));
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EXECUTABLEENTITYDOMAIN, HAPUtilityJson.buildJson(this.m_executableEntityDomain, HAPSerializationFormat.JSON));
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
