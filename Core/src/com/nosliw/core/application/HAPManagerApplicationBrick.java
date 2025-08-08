package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.resource.HAPIdResourceType;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.resource.HAPResourceIdEmbeded;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.system.HAPSystemUtility;

@Component
public class HAPManagerApplicationBrick {

	private Map<String, HAPPluginDivision> m_divisionPlugin;
	
	//some brick type blong to particular division
	private Map<HAPIdBrickType, String> m_divisionByBrickType;

	private Map<String, HAPIdBrickType> m_adapterTypeByBlockType;
	
	private Map<String, Map<String, HAPPluginBrick>> m_brickPlugins;

	public HAPManagerApplicationBrick(List<HAPProviderPluginBrick> brickPluginProviders) {
		this.m_divisionByBrickType = new LinkedHashMap<HAPIdBrickType, String>();
		this.m_brickPlugins = new LinkedHashMap<String, Map<String, HAPPluginBrick>>();
		this.m_divisionPlugin = new LinkedHashMap<String, HAPPluginDivision>();
		
		for(HAPProviderPluginBrick pluginProvider : brickPluginProviders) {
			for(HAPPluginBrick brickPlugin : pluginProvider.getBrickPlugins()) {
				this.registerBrickPlugin(brickPlugin);
			}
		}
	}
	
	@Autowired
	private void setDivisionPlugins(List<HAPPluginDivision> divisionPlugins) {
		for(HAPPluginDivision divisionPlugin : divisionPlugins) {
			this.m_divisionPlugin.put(divisionPlugin.getDivisionName(), divisionPlugin);
		}
	}
	
	public HAPBundle getBrickBundle(HAPIdBrick brickId, HAPRuntimeInfo runtimeInfo) {
		String division = brickId.getDivision();
		if(division==null) {
			division = this.m_divisionByBrickType.get(brickId.getBrickTypeId());
		}
		if(division==null) {
			division = HAPSystemUtility.getDefaultDivision();
		}
		
		HAPPluginDivision divisionPlugin = this.m_divisionPlugin.get(division);
		HAPBundle bundle = divisionPlugin.getBundle(brickId, runtimeInfo);
		
		List<HAPInfoExportResource> exposes = this.getBrickPlugin(brickId.getBrickTypeId()).getExposeResourceInfo(bundle.getMainBrickWrapper().getBrick());
		for(HAPInfoExportResource expose : exposes) {
			bundle.addExportResourceInfo(expose);
		}
		
		return bundle;
	}
	
	public HAPApplicationPackage getBrickPackage(HAPResourceId resourceId) {
		HAPApplicationPackage out = new HAPApplicationPackage();

		//figure out root entity
		out.setMainResourceId(resourceId);
		
		//find all related complex resource
		Set<HAPResourceId> dependency = new HashSet<HAPResourceId>();
//		buildDependencyGroup(resourceId, dependency);
		for(HAPResourceId bundleId : dependency) {
			out.addDependency(bundleId);
		}
		
//		HAPUtilityExport.exportEntityPackage(out, this, this.m_runtimeEnv.getRuntime().getRuntimeInfo());
		return out;
	}
	

	
	public List<HAPIdBrickType> getAllVersions(String brickType){
		List<HAPPluginBrick> brickPlugins = new ArrayList<HAPPluginBrick>(this.m_brickPlugins.get(brickType).values());
		Collections.sort(brickPlugins, new Comparator<HAPPluginBrick>(){
			@Override
			public int compare(HAPPluginBrick arg0, HAPPluginBrick arg1) {
				return arg0.getBrickType().getVersion().compareTo(arg1.getBrickType().getVersion());
			}
		});
		
		List<HAPIdBrickType> out = new ArrayList<HAPIdBrickType>();
		for(HAPPluginBrick brickPlugin : brickPlugins) {
			out.add(brickPlugin.getBrickType());
		}
		
		return out;
	}
	
	public HAPIdBrickType getLatestVersion(String entityType) {
		return this.getAllVersions(entityType).get(0);
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

	public HAPIdBrickType getBrickTypeIdFromResourceTypeId(HAPIdResourceType resourceTypeId) {
		HAPIdBrickType out = null;
		//whether have brick type match resource type
		if(m_brickPlugins.get(resourceTypeId.getResourceType())!=null) {
			out = HAPUtilityBrickId.getBrickTypeIdFromResourceTypeId(resourceTypeId);
		}
		return out;
	}

	//add division information to resource id if missing
	public HAPResourceId normalizeResourceIdWithDivision(HAPResourceId resourceId, String divisionDefault) {
		HAPResourceId out = resourceId;
		String strucuture = resourceId.getStructure();
		if(HAPConstantShared.RESOURCEID_TYPE_SIMPLE.equals(strucuture)) {
			HAPResourceIdSimple simpleResourceId = (HAPResourceIdSimple)out;
			HAPIdBrickType brickTypeId = getBrickTypeIdFromResourceTypeId(simpleResourceId.getResourceTypeId());
			if(brickTypeId!=null) {
				//for known brick
				String[] segs = HAPUtilityNamingConversion.parseLevel1(simpleResourceId.getId());
				if(segs.length<=1) {
					String id = segs[0];
					String division = this.m_divisionByBrickType.get(brickTypeId);
					if(division==null) {
						division = divisionDefault;
					}
					simpleResourceId.setId(HAPUtilityNamingConversion.cascadeLevel1(id, division));
				}
			}
		}
		else if(HAPConstantShared.RESOURCEID_TYPE_EMBEDED.equals(strucuture)) {
			HAPResourceIdEmbeded embededResourceId = (HAPResourceIdEmbeded)out;
			embededResourceId.setParentResourceId((HAPResourceIdSimple)normalizeResourceIdWithDivision(embededResourceId.getParentResourceId(), divisionDefault));
		}
		return out;
	}
	
	public void registerBrickPlugin(HAPPluginBrick brickPlugin) {
		HAPIdBrickType entityTypeId = brickPlugin.getBrickType();
		
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

	
	private void buildDependencyGroup(HAPResourceId complexEntityResourceId, Set<HAPResourceId> dependency) {
		if(!dependency.contains(complexEntityResourceId)) {
			dependency.add(complexEntityResourceId);

			HAPBundle bundle = this.getBrickBundle(HAPUtilityBrickId.parseBrickId(complexEntityResourceId));
			Set<HAPResourceIdSimple> bundleDependency = bundle.getResourceDependency();
			for(HAPResourceIdSimple id : bundleDependency) {
				buildDependencyGroup(id, dependency);
			}
		}
	}


}
