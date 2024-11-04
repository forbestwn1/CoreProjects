package com.nosliw.core.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.resource.HAPResourceDataBrick;
import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.resource.HAPWithResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPBundle extends HAPSerializableImp implements HAPWithResourceDependency{

	@HAPAttribute
	public final static String MAINBRICK = "mainBrick"; 

	@HAPAttribute
	public static final String VALUESTRUCTUREDOMAIN = "valueStructureDomain";

	@HAPAttribute
	public final static String EXTRADATA = "extraData"; 

	private HAPWrapperBrickRoot m_mainBrickWrapper;

	//other brick that support main brick, for instance, global task
	private Map<String, HAPWrapperBrickRoot> m_supportBrick;
	
	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;
	
	private Object m_extraData;

	private List<HAPInfoExportResource> m_exportResourceInfos;
	
	public HAPBundle() {
		this.m_valueStructureDomain = new HAPDomainValueStructure();
		this.m_exportResourceInfos = new ArrayList<HAPInfoExportResource>();
		
		HAPInfoExportResource defaultExport = new HAPInfoExportResource(new HAPPath());
		defaultExport.setName(HAPConstantShared.NAME_DEFAULT);
		this.addExportResourceInfo(defaultExport);
	}
	
	public void addExportResourceInfo(HAPInfoExportResource exportResourceInfo) {
		this.m_exportResourceInfos.add(exportResourceInfo);
	}
	
	public HAPResourceDataBrick getExportResourceData(String name, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		if(name==null) {
			name = HAPConstantShared.NAME_DEFAULT;
		}
		HAPInfoExportResource exportInfo = null;
		for(HAPInfoExportResource ei : this.m_exportResourceInfos) {
			if(name.equals(ei.getName())) {
				exportInfo = ei;
				break;
			}
		}
		
		HAPResourceDataBrick out = null;
		HAPResultBrick brickResult = HAPUtilityBrick.getDescdentBrickResult(this, exportInfo.getPathFromRoot());
		if(brickResult.isInternalBrick()) {
			HAPBrick brick = brickResult.getInternalBrick();
			out = new HAPResourceDataBrick(brick, this.m_valueStructureDomain);
		}
		else {
			out = (HAPResourceDataBrick)HAPUtilityResource.getResource(brickResult.getResourceId(), resourceMan, runtimeInfo).getResourceData();
		}
		return out;
	}
	
	public HAPDomainValueStructure getValueStructureDomain() {	return this.m_valueStructureDomain;	}
	
	public HAPWrapperBrickRoot getMainBrickWrapper() {    return this.m_mainBrickWrapper;     }
	public void setMainBrickWrapper(HAPWrapperBrickRoot brickWrapper) {     this.m_mainBrickWrapper = brickWrapper;      }
	
	public Object getExtraData() {   return this.m_extraData;    }
	public void setExtraData(Object data) {   this.m_extraData = data;    }

	
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
		jsonMap.put(EXTRADATA, HAPManagerSerialize.getInstance().toStringValue(m_extraData, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MAINBRICK, this.m_mainBrickWrapper.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		jsonMap.put(VALUESTRUCTUREDOMAIN, this.m_valueStructureDomain.toStringValue(HAPSerializationFormat.JAVASCRIPT));
		jsonMap.put(EXTRADATA, HAPManagerSerialize.getInstance().toStringValue(m_extraData, HAPSerializationFormat.JAVASCRIPT));
	}
	
	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_mainBrickWrapper.buildResourceDependency(dependency, runtimeInfo);
	}
}
