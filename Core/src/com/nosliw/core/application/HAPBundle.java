package com.nosliw.core.application;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPBundle extends HAPExecutableImp{

	@HAPAttribute
	public final static String BRICK = "brick"; 

	@HAPAttribute
	public final static String EXTRADATA = "extraData"; 

	private HAPWrapperBrickRoot m_brickWrapper;

	private Object m_extraData;

	private List<HAPInfoExportBundle> m_exportInfos;
	
	
	public HAPWrapperBrickRoot getBrickWrapper() {    return this.m_brickWrapper;     }
	public void setBrickWrapper(HAPWrapperBrickRoot brickWrapper) {     this.m_brickWrapper = brickWrapper;      }
	
	public Object getExtraData() {   return this.m_extraData;    }
	public void setExtraData(Object data) {   this.m_extraData = data;    }

	
	public Set<HAPResourceIdSimple> getComplexResourceDependency(){
		Set<HAPResourceIdSimple> out = new HashSet<HAPResourceIdSimple>();
//		for(HAPInfoResourceIdNormalize normalizedResourceId : this.m_externalComplexEntityDpendency) {
//			out.add(normalizedResourceId.getRootResourceIdSimple());
//		}
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICK, this.m_brickWrapper.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(EXTRADATA, HAPSerializeManager.getInstance().toStringValue(m_extraData, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(BRICK, this.m_brickWrapper.toResourceData(runtimeInfo).toString());
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		dependency.addAll(this.m_brickWrapper.getResourceDependency(runtimeInfo, resourceManager));
	}
}
