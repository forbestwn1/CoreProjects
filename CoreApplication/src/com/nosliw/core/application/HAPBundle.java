package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.dynamic.HAPDynamicDefinitionContainer;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.resource.HAPWithResourceDependency;
import com.nosliw.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPBundle extends HAPSerializableImp implements HAPWithResourceDependency{

	@HAPAttribute
	public final static String MAINBRICK = "mainBrick"; 

	@HAPAttribute
	public final static String BRANCHBRICKS = "branchBricks"; 

	@HAPAttribute
	public static final String VALUESTRUCTUREDOMAIN = "valueStructureDomain";

	@HAPAttribute
	public final static String DYNAMIC = "dynamic"; 

	@HAPAttribute
	public final static String ALIASMAPPING = "aliasMapping"; 

	@HAPAttribute
	public final static String EXTRADATA = "extraData"; 

	private HAPWrapperBrickRoot m_mainBrickWrapper;

	//other brick that support main brick, for instance, global task
	private Map<String, HAPWrapperBrickRoot> m_branchBricks;
	
	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;
	
	//need dynamic input during runtime
	private HAPDynamicDefinitionContainer m_dynamicInfo;

	private Map<String, HAPPath> m_aliasMapping;
	
	private Object m_extraData;

	private List<HAPInfoExportBrick> m_exportResourceInfos;
	
	public HAPBundle() {
		this.m_valueStructureDomain = new HAPDomainValueStructure();
		this.m_exportResourceInfos = new ArrayList<HAPInfoExportBrick>();
		this.m_branchBricks = new LinkedHashMap<String, HAPWrapperBrickRoot>();
		this.m_aliasMapping = new LinkedHashMap<String, HAPPath>();
	
		this.m_dynamicInfo = new HAPDynamicDefinitionContainer(); 
		
		HAPInfoExportBrick defaultExport = new HAPInfoExportBrick(new HAPPath());
		defaultExport.setName(HAPConstantShared.NAME_DEFAULT);
		this.addExportResourceInfo(defaultExport);
	}
	
	public void addExportResourceInfo(HAPInfoExportBrick exportResourceInfo) {		this.m_exportResourceInfos.add(exportResourceInfo);	}
	public List<HAPInfoExportBrick> getExportResourceInfos(){    return this.m_exportResourceInfos;    }
	
	public HAPDomainValueStructure getValueStructureDomain() {	return this.m_valueStructureDomain;	}
	
	public void addRootBrickWrapper(HAPWrapperBrickRoot brickWrapper) {
		String name = brickWrapper.getName();
		if(HAPConstantShared.NAME_ROOTBRICK_MAIN.equals(name)) {
			this.setMainBrickWrapper(brickWrapper);
		}
		else {
			this.setBranchBrickWrapper(name, brickWrapper);
		}
	}
	public HAPWrapperBrickRoot getRootBrickWrapper(String name) {
		if(HAPConstantShared.NAME_ROOTBRICK_MAIN.equals(name)) {
			return this.getMainBrickWrapper();
		}
		else {
			return this.getBranchBrickWrapper(name);
		}
	}
	
	public HAPWrapperBrickRoot getMainBrickWrapper() {    return this.m_mainBrickWrapper;     }
	public void setMainBrickWrapper(HAPWrapperBrickRoot brickWrapper) {     this.m_mainBrickWrapper = brickWrapper;      }
	
	public void setBranchBrickWrapper(String branch, HAPWrapperBrickRoot brickWrapper) {     this.m_branchBricks.put(branch, brickWrapper);        }
	private HAPWrapperBrickRoot getBranchBrickWrapper(String branch) {     return this.m_branchBricks.get(branch);         }
	public Map<String, HAPWrapperBrickRoot> getBranchBrickWrappers() {     return this.m_branchBricks;         }
	public Set<String> getBranchNames(){   return this.m_branchBricks.keySet();    }
	
	public HAPDynamicDefinitionContainer getDynamicInfo() {     return this.m_dynamicInfo;        }
	
	public Object getExtraData() {   return this.m_extraData;    }
	public void setExtraData(Object data) {   this.m_extraData = data;    }

	public Map<String, HAPPath> getAliasMappings(){    return this.m_aliasMapping;     }
	public void addAliasMapping(String alias, HAPPath path) {    this.m_aliasMapping.put(alias, path);       }
	public HAPPath getBrickPathByAlias(String alias) {    return this.m_aliasMapping.get(alias);      }
	
	public Set<HAPResourceIdSimple> getResourceDependency(){
		Set<HAPResourceIdSimple> out = new HashSet<HAPResourceIdSimple>();
//		for(HAPInfoResourceIdNormalize normalizedResourceId : this.m_externalComplexEntityDpendency) {
//			out.add(normalizedResourceId.getRootResourceIdSimple());
//		}
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MAINBRICK, this.m_mainBrickWrapper.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(VALUESTRUCTUREDOMAIN, this.m_valueStructureDomain.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(DYNAMIC, this.m_dynamicInfo.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EXTRADATA, HAPManagerSerialize.getInstance().toStringValue(m_extraData, HAPSerializationFormat.JSON));
		jsonMap.put(ALIASMAPPING, HAPUtilityJson.buildJsonStringValue(this.m_aliasMapping, HAPSerializationFormat.JSON));
		
		Map<String, String> branchJsonMap = new LinkedHashMap<String, String>();
		for(String branch : this.m_branchBricks.keySet()) {
			branchJsonMap.put(branch, this.m_branchBricks.get(branch).toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(BRANCHBRICKS, HAPUtilityJson.buildMapJson(branchJsonMap));
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MAINBRICK, this.m_mainBrickWrapper.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		jsonMap.put(VALUESTRUCTUREDOMAIN, this.m_valueStructureDomain.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		jsonMap.put(DYNAMIC, this.m_dynamicInfo.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		jsonMap.put(EXTRADATA, HAPManagerSerialize.getInstance().toStringValue(m_extraData, HAPSerializationFormat.JAVASCRIPT));
		jsonMap.put(ALIASMAPPING, HAPUtilityJson.buildJsonStringValue(this.m_aliasMapping, HAPSerializationFormat.JAVASCRIPT));

		Map<String, String> branchJsonMap = new LinkedHashMap<String, String>();
		for(String branch : this.m_branchBricks.keySet()) {
			branchJsonMap.put(branch, this.m_branchBricks.get(branch).toStringValue(HAPSerializationFormat.JAVASCRIPT));
		}
		jsonMap.put(BRANCHBRICKS, HAPUtilityJson.buildMapJson(branchJsonMap));
	}
	
	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_mainBrickWrapper.buildResourceDependency(dependency, runtimeInfo);
	}
}
