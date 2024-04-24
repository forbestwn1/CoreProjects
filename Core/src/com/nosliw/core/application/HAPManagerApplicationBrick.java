package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.core.application.brick.test.complex.script.HAPBrickTestComplexScript;
import com.nosliw.core.application.brick.test.complex.testcomplex1.HAPBrickTestComplex1;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManagerApplicationBrick {

	private Map<String, HAPPluginDivision> m_divisionPlugin;
	
	//
	private Map<HAPIdBrickType, String> m_divisionByBrickType;
	
	private Map<String, Map<String, HAPPluginBrick>> m_brickPlugins;

	private Map<String, HAPIdBrickType> m_adapterTypeByBlockType;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPManagerApplicationBrick(HAPRuntimeEnvironment runtimeEnv) {
		this.m_divisionPlugin = new LinkedHashMap<String, HAPPluginDivision>();
		this.m_divisionByBrickType = new LinkedHashMap<HAPIdBrickType, String>();
		this.m_brickPlugins = new LinkedHashMap<String, Map<String, HAPPluginBrick>>();
		this.m_runtimeEnv = runtimeEnv;
		this.init();
	}
	
	private void init() {

		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.TEST_COMPLEX_1_100, true), HAPBrickTestComplex1.class, this));
		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, true), HAPBrickTestComplexScript.class, this));

		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, true), HAPBrickTestComplexScript.class, this));
	}

	public HAPInfoBrickType getBrickTypeInfo(HAPIdBrickType brickTypeId) {		return this.getBrickPlugin(brickTypeId).getBrickTypeInfo();	}
	
	public HAPBrick newBrickInstance(HAPIdBrickType brickTypeId) {		return this.getBrickPlugin(brickTypeId).newInstance();	}

	public List<HAPIdBrickType> getAllVersions(String brickType){
		List<HAPPluginBrick> brickPlugins = new ArrayList<HAPPluginBrick>(this.m_brickPlugins.get(brickType).values());
		Collections.sort(brickPlugins, new Comparator<HAPPluginBrick>(){
			@Override
			public int compare(HAPPluginBrick arg0, HAPPluginBrick arg1) {
				return arg0.getBrickTypeInfo().getBrickTypeId().getVersion().compareTo(arg1.getBrickTypeInfo().getBrickTypeId().getVersion());
			}
		});
		
		List<HAPIdBrickType> out = new ArrayList<HAPIdBrickType>();
		for(HAPPluginBrick brickPlugin : brickPlugins) {
			out.add(brickPlugin.getBrickTypeInfo().getBrickTypeId());
		}
		
		return out;
	}
	
	public HAPIdBrickType getLatestVersion(String entityType) {
		return this.getAllVersions(entityType).get(0);
	}

	public HAPBundle getBrickBundle(HAPIdBrick brickId) {
		String division = brickId.getDivision();
		if(division==null) {
			division = this.m_divisionByBrickType.get(brickId.getBrickTypeId());
		}
		
		HAPPluginDivision divisionPlugin = this.m_divisionPlugin.get(division);
		return divisionPlugin.getBundle(brickId);
	}
	
	public HAPBundle getBrickBundle(HAPResourceIdSimple resourceId) {
		return this.getBrickBundle(HAPUtilityBrick.parseBrickId(resourceId));
	}

	public HAPApplicationPackage getBrickPackage(HAPResourceId resourceId) {
		HAPApplicationPackage out = new HAPApplicationPackage();

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

			HAPBundle bundle = this.getBrickBundle(HAPUtilityBrick.parseBrickId(complexEntityResourceId));
			Set<HAPResourceIdSimple> bundleDependency = bundle.getComplexResourceDependency();
			for(HAPResourceIdSimple id : bundleDependency) {
				buildDependencyGroup(id, dependency);
			}
		}
	}

	public void registerDivisionInfo(String division, HAPPluginDivision divisionPlugin) {
		this.m_divisionPlugin.put(division, divisionPlugin);
		Set<HAPIdBrickType> brickTypes = divisionPlugin.getBrickTypes();
		if(brickTypes!=null) {
			for(HAPIdBrickType brickType : brickTypes) {
				this.m_divisionByBrickType.put(brickType, division);
			}
		}
	}

	public void registerBrickPlugin(HAPPluginBrick brickPlugin) {
		HAPIdBrickType entityTypeId = brickPlugin.getBrickTypeInfo().getBrickTypeId();
		
		Map<String, HAPPluginBrick> byVersion = this.m_brickPlugins.get(entityTypeId.getBrickType());
		if(byVersion==null) {
			byVersion = new LinkedHashMap<String, HAPPluginBrick>();
			this.m_brickPlugins.put(entityTypeId.getBrickType(), byVersion);
		}
		
		byVersion.put(entityTypeId.getVersion(), brickPlugin);
	}
	
	public void registerAdapterTypeByBlockType(HAPIdBrickType blockType, HAPIdBrickType adapterType) {		this.m_adapterTypeByBlockType.put(blockType.getKey(), adapterType);	}
	public HAPIdBrickType getDefaultAdapterTypeByBlockType(HAPIdBrickType blockType) {   return this.m_adapterTypeByBlockType.get(blockType.getKey());  	}
	
	private HAPPluginBrick getBrickPlugin(HAPIdBrickType brickTypeId) {		return this.m_brickPlugins.get(brickTypeId.getBrickType()).get(brickTypeId.getVersion());	}

}
