package com.nosliw.data.core.entity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.entity.division.manual.HAPManagerEntityDivisionManual;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerEntity {

	private Map<String, Map<String, HAPInfoEntityType>> m_entityTypeInfos;
	
	private Map<String, HAPInfoDivision> m_divisionInfo;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManagerEntity(HAPRuntimeEnvironment runtimeEnv) {
		this.m_entityTypeInfos = new LinkedHashMap<String, Map<String, HAPInfoEntityType>>();
		this.m_divisionInfo = new LinkedHashMap<String, HAPInfoDivision>();
		this.m_runtimeEnv = runtimeEnv;
		this.init();
	}
	
	private void init() {
		this.registerEntityTypeInfo(new HAPInfoEntityType(HAPEnumEntityType.TEST_COMPLEX_1_100, true));
		this.registerEntityTypeInfo(new HAPInfoEntityType(HAPEnumEntityType.TEST_COMPLEX_SCRIPT_100, true));
		
		this.registerEntityTypeInfo(new HAPInfoEntityType(HAPEnumEntityType.VALUESTRUCTURE_100, false));
		this.registerEntityTypeInfo(new HAPInfoEntityType(HAPEnumEntityType.VALUECONTEXT_100, false));
		
		this.registerDivisionInfo(HAPConstantShared.ENTITY_DIVISION_MANUAL, new HAPInfoDivision(new HAPPluginRepositoryEntityPackageImpDummy(), new HAPManagerEntityDivisionManual(this.m_runtimeEnv)));
	}
	
	public HAPInfoEntityType getEntityTypeInfo(HAPIdEntityType entityTypeId) {
		return this.m_entityTypeInfos.get(entityTypeId.getEntityType()).get(entityTypeId.getVersion());
	}

	public List<HAPIdEntityType> getAllVersions(String entityType){
		List<HAPInfoEntityType> entityTypeInfos = new ArrayList<HAPInfoEntityType>(this.m_entityTypeInfos.get(entityType).values());
		Collections.sort(entityTypeInfos, new Comparator<HAPInfoEntityType>(){
			@Override
			public int compare(HAPInfoEntityType arg0, HAPInfoEntityType arg1) {
				return arg0.getEntityTypeId().getVersion().compareTo(arg1.getEntityTypeId().getVersion());
			}
		});
		
		List<HAPIdEntityType> out = new ArrayList<HAPIdEntityType>();
		for(HAPInfoEntityType entityTypeInfo : entityTypeInfos) {
			out.add(entityTypeInfo.getEntityTypeId());
		}
		
		return out;
	}
	
	public HAPIdEntityType getLatestVersion(String entityType) {
		return this.getAllVersions(entityType).get(0);
	}

	public HAPEntityBundle getEntityBundle(HAPResourceIdSimple resourceId) {
		return this.getEntityBundle(HAPUtilityEntity.createEntityId(resourceId));
	}

	public HAPEntityBundle getEntityBundle(HAPIdEntity entityId) {
		HAPEntityBundle out = null;
		
		HAPInfoDivision divisionInfo = this.m_divisionInfo.get(entityId.getDivision());

		//retrieve from respository first
		out = divisionInfo.getEntityPackageRepositoryPlugin().retrieveEntityPackage(entityId);
		
		if(out==null) {
			//process definition
			out = divisionInfo.getEntityProcessorPlugin().process(entityId);
		}
		return out;
	}
	
	public HAPEntityPackage getPackage(HAPResourceId resourceId) {
		HAPEntityPackage out = new HAPEntityPackage();

		//figure out root entity
		HAPInfoResourceIdNormalize normalizedResourceId = HAPUtilityResourceId.normalizeResourceId(resourceId);
		out.setMainEntityId(normalizedResourceId);
		
		//find all related complex resource
		Set<HAPResourceIdSimple> dependency = new HashSet<HAPResourceIdSimple>();
		buildDependencyGroup(normalizedResourceId.getRootResourceIdSimple(), dependency);
		for(HAPResourceIdSimple bundleId : dependency) {
			out.addDependency(bundleId);
		}
		
		HAPUtilityExport.exportEntityPackage(out, this, this.m_runtimeEnv.getRuntime().getRuntimeInfo());
		return out;
	}
	
	private void buildDependencyGroup(HAPResourceIdSimple complexEntityResourceId, Set<HAPResourceIdSimple> dependency) {
		if(!dependency.contains(complexEntityResourceId)) {
			dependency.add(complexEntityResourceId);

			HAPEntityBundle bundle = this.getEntityBundle(HAPUtilityEntity.createEntityId(complexEntityResourceId));
			Set<HAPResourceIdSimple> bundleDependency = bundle.getComplexResourceDependency();
			for(HAPResourceIdSimple id : bundleDependency) {
				buildDependencyGroup(id, dependency);
			}
		}
	}

	public void registerDivisionInfo(String division, HAPInfoDivision divisionInfo) {
		this.m_divisionInfo.put(division, divisionInfo);
	}

	public void registerEntityTypeInfo(HAPInfoEntityType entityTypeInfo) {
		HAPIdEntityType entityTypeId = entityTypeInfo.getEntityTypeId();
		
		Map<String, HAPInfoEntityType> byVersion = this.m_entityTypeInfos.get(entityTypeId.getEntityType());
		if(byVersion==null) {
			byVersion = new LinkedHashMap<String, HAPInfoEntityType>();
			this.m_entityTypeInfos.put(entityTypeId.getEntityType(), byVersion);
		}
		
		byVersion.put(entityTypeId.getVersion(), entityTypeInfo);
	}
	
}
