package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPManagerEntityDivisionManual;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerApplicationBrick {

	private Map<String, Map<String, HAPInfoBrickType>> m_brickTypeInfos;
	
	private Map<String, HAPInfoBrickDivision> m_divisionInfo;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManagerApplicationBrick(HAPRuntimeEnvironment runtimeEnv) {
		this.m_brickTypeInfos = new LinkedHashMap<String, Map<String, HAPInfoBrickType>>();
		this.m_divisionInfo = new LinkedHashMap<String, HAPInfoBrickDivision>();
		this.m_runtimeEnv = runtimeEnv;
		this.init();
	}
	
	private void init() {
		this.registerEntityTypeInfo(new HAPInfoBrickType(HAPEnumBrickType.TEST_COMPLEX_1_100, true));
		this.registerEntityTypeInfo(new HAPInfoBrickType(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, true));
		
		this.registerEntityTypeInfo(new HAPInfoBrickType(HAPEnumBrickType.VALUESTRUCTURE_100, false));
		this.registerEntityTypeInfo(new HAPInfoBrickType(HAPEnumBrickType.VALUECONTEXT_100, false));
		
		this.registerDivisionInfo(HAPConstantShared.ENTITY_DIVISION_MANUAL, new HAPInfoBrickDivision(new HAPPluginRepositoryBundleImpDummy(), new HAPManagerEntityDivisionManual(this.m_runtimeEnv)));
	}
	
	public HAPInfoBrickType getBrickTypeInfo(HAPIdBrickType entityTypeId) {
		return this.m_brickTypeInfos.get(entityTypeId.getBrickType()).get(entityTypeId.getVersion());
	}

	public List<HAPIdBrickType> getAllVersions(String entityType){
		List<HAPInfoBrickType> entityTypeInfos = new ArrayList<HAPInfoBrickType>(this.m_brickTypeInfos.get(entityType).values());
		Collections.sort(entityTypeInfos, new Comparator<HAPInfoBrickType>(){
			@Override
			public int compare(HAPInfoBrickType arg0, HAPInfoBrickType arg1) {
				return arg0.getEntityTypeId().getVersion().compareTo(arg1.getEntityTypeId().getVersion());
			}
		});
		
		List<HAPIdBrickType> out = new ArrayList<HAPIdBrickType>();
		for(HAPInfoBrickType entityTypeInfo : entityTypeInfos) {
			out.add(entityTypeInfo.getEntityTypeId());
		}
		
		return out;
	}
	
	public HAPIdBrickType getLatestVersion(String entityType) {
		return this.getAllVersions(entityType).get(0);
	}

	public HAPBundle getEntityBundle(HAPResourceIdSimple resourceId) {
		return this.getEntityBundle(HAPUtilityBrick.parseBrickId(resourceId));
	}

	public HAPBundle getEntityBundle(HAPIdBrick entityId) {
		HAPBundle out = null;
		
		HAPInfoBrickDivision divisionInfo = this.m_divisionInfo.get(entityId.getDivision());

		//retrieve from respository first
		out = divisionInfo.getEntityPackageRepositoryPlugin().retrieveEntityPackage(entityId);
		
		if(out==null) {
			//process definition
			out = divisionInfo.getEntityProcessorPlugin().process(entityId);
		}
		return out;
	}
	
	public HAPPackage getEntityPackage(HAPResourceId resourceId) {
		HAPPackage out = new HAPPackage();

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

			HAPBundle bundle = this.getEntityBundle(HAPUtilityBrick.parseBrickId(complexEntityResourceId));
			Set<HAPResourceIdSimple> bundleDependency = bundle.getComplexResourceDependency();
			for(HAPResourceIdSimple id : bundleDependency) {
				buildDependencyGroup(id, dependency);
			}
		}
	}

	public void registerDivisionInfo(String division, HAPInfoBrickDivision divisionInfo) {
		this.m_divisionInfo.put(division, divisionInfo);
	}

	public void registerEntityTypeInfo(HAPInfoBrickType entityTypeInfo) {
		HAPIdBrickType entityTypeId = entityTypeInfo.getEntityTypeId();
		
		Map<String, HAPInfoBrickType> byVersion = this.m_brickTypeInfos.get(entityTypeId.getBrickType());
		if(byVersion==null) {
			byVersion = new LinkedHashMap<String, HAPInfoBrickType>();
			this.m_brickTypeInfos.put(entityTypeId.getBrickType(), byVersion);
		}
		
		byVersion.put(entityTypeId.getVersion(), entityTypeInfo);
	}
	
}
