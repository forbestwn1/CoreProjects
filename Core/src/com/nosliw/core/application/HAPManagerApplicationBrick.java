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
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.brick.adapter.dataassociation.HAPAdapterDataAssociation;
import com.nosliw.core.application.brick.adapter.dataassociationfortask.HAPAdapterDataAssociationForTask;
import com.nosliw.core.application.brick.container.HAPBrickContainer;
import com.nosliw.core.application.brick.dataexpression.lib.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.brick.dataexpression.lib.HAPBlockDataExpressionLibrary;
import com.nosliw.core.application.brick.interactive.interfacee.HAPBlockInteractiveInterface;
import com.nosliw.core.application.brick.service.interfacee.HAPBlockServiceInterface;
import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;
import com.nosliw.core.application.brick.service.provider.HAPBlockServiceProvider;
import com.nosliw.core.application.brick.taskwrapper.HAPBlockTaskWrapper;
import com.nosliw.core.application.brick.test.complex.script.HAPBlockTestComplexScript;
import com.nosliw.core.application.brick.test.complex.testcomplex1.HAPBlockTestComplex1;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdEmbeded;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
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

		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.TEST_COMPLEX_1_100, true), HAPBlockTestComplex1.class, this.m_runtimeEnv));
		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.TEST_COMPLEX_SCRIPT_100, true), HAPBlockTestComplexScript.class, this.m_runtimeEnv));

		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.TASKWRAPPER_100, false), HAPBlockTaskWrapper.class, this.m_runtimeEnv));
		
		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.SERVICEPROVIDER_100, false), HAPBlockServiceProvider.class, this.m_runtimeEnv));
		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.SERVICEPROFILE_100, false), HAPBlockServiceProfile.class, this.m_runtimeEnv));
		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.SERVICEINTERFACE_100, false), HAPBlockServiceInterface.class, this.m_runtimeEnv));

		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.INTERACTIVEINTERFACE_100, false), HAPBlockInteractiveInterface.class, this.m_runtimeEnv));

		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.DATAEXPRESSIONLIB_100, false), HAPBlockDataExpressionLibrary.class, this.m_runtimeEnv));
		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, false), HAPBlockDataExpressionElementInLibrary.class, this.m_runtimeEnv));

		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.CONTAINER_100, false), HAPBrickContainer.class, this.m_runtimeEnv));
		
		
		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.DATAASSOCIATION_100, false), HAPAdapterDataAssociation.class, this.m_runtimeEnv));
		this.registerBrickPlugin(new HAPPluginBrickImp(new HAPInfoBrickType(HAPEnumBrickType.DATAASSOCIATIONFORTASK_100, false), HAPAdapterDataAssociationForTask.class, this.m_runtimeEnv));
		
		
		
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
		out.setMainResourceId(resourceId);
		
		//find all related complex resource
		Set<HAPResourceId> dependency = new HashSet<HAPResourceId>();
//		buildDependencyGroup(resourceId, dependency);
		for(HAPResourceId bundleId : dependency) {
			out.addDependency(bundleId);
		}
		
		HAPUtilityExport.exportEntityPackage(out, this, this.m_runtimeEnv.getRuntime().getRuntimeInfo());
		return out;
	}
	
	private void buildDependencyGroup(HAPResourceId complexEntityResourceId, Set<HAPResourceId> dependency) {
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

	//add division information to resource id if missing
	public HAPResourceId normalizeResourceIdWithDivision(HAPResourceId resourceId, String divisionDefault) {
		HAPResourceId out = resourceId;
		String strucuture = resourceId.getStructure();
		if(HAPConstantShared.RESOURCEID_TYPE_SIMPLE.equals(strucuture)) {
			HAPResourceIdSimple simpleResourceId = (HAPResourceIdSimple)out;
			if(m_brickPlugins.get(simpleResourceId.getResourceTypeId().getResourceType())!=null) {
				//for known brick
				String[] segs = HAPUtilityNamingConversion.parseLevel1(simpleResourceId.getId());
				if(segs.length<=1) {
					String id = segs[0];
					String division = this.m_divisionByBrickType.get(HAPUtilityBrick.getBrickTypeIdFromResourceTypeId(simpleResourceId.getResourceTypeId()));
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
