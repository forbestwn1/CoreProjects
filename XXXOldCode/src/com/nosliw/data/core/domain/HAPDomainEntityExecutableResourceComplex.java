package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPDomainEntityExecutableResourceComplex extends HAPExecutableImp implements HAPDomainEntity{

	@HAPAttribute
	public static String VALUESTRUCTUREDOMAIN = "valueStructureDomain";
	@HAPAttribute
	public static String ROOTENTITY = "rootEntity";
	@HAPAttribute
	public static String EXTERNALENTITY = "externalEntity";

	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;

	//all other complex resource this resource depend on, external dependency
	private Set<HAPInfoResourceIdNormalize> m_externalComplexEntityDpendency;

	private HAPExecutableEntityComplex m_rootEntity;
	
	public HAPDomainEntityExecutableResourceComplex() {
		this.m_valueStructureDomain = new HAPDomainValueStructure();
		this.m_externalComplexEntityDpendency = new HashSet<HAPInfoResourceIdNormalize>();
	}

	public void setRootEntity(HAPExecutableEntityComplex rootEntity) {   this.m_rootEntity = rootEntity;     }
	public HAPExecutableEntityComplex getRootEntity() {    return this.m_rootEntity;     }
	
	public HAPDomainValueStructure getValueStructureDomain() {    return this.m_valueStructureDomain;     }
	
	public void addExternalResourceDependency(HAPInfoResourceIdNormalize normlizedResourceId) {   this.m_externalComplexEntityDpendency.add(normlizedResourceId);      }

	@Override
	public HAPInfoEntityInDomain getEntityInfo(HAPIdEntityInDomain entityId) {    return null;     }

	public Set<HAPResourceIdSimple> getComplexResourceDependency(){
		Set<HAPResourceIdSimple> out = new HashSet<HAPResourceIdSimple>();
		for(HAPInfoResourceIdNormalize normalizedResourceId : this.m_externalComplexEntityDpendency) {
			out.add(normalizedResourceId.getRootResourceId());
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTUREDOMAIN, HAPUtilityJson.buildJson(this.m_valueStructureDomain, HAPSerializationFormat.JSON));
		jsonMap.put(ROOTENTITY, this.m_rootEntity.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EXTERNALENTITY, HAPUtilityJson.buildJson(this.m_externalComplexEntityDpendency, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(ROOTENTITY, this.m_rootEntity.toResourceData(runtimeInfo).toString());
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(super.getResourceDependency(runtimeInfo, resourceManager));
		this.buildResourceDependencyForExecutable(out, this.m_rootEntity, runtimeInfo, resourceManager);
		return out;
	}
}
