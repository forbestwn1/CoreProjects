package com.nosliw.core.application;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPAttributeInBrick extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public static final String VALUEWRAPPER = "valueWrapper";

	@HAPAttribute
	public static final String PATHFROMROOT = "pathFromRoot";

	private HAPWrapperValueInAttribute m_valueWrapper;
	
	public HAPAttributeInBrick() {}
	
	public HAPAttributeInBrick(String attrName, HAPWrapperValueInAttribute valueWrapper) {
		this.setName(attrName);
		this.setValueInfo(valueWrapper);
	}
	
	public HAPWrapperValueInAttribute getValueWrapper() {	return this.m_valueWrapper;	}
	
	public void setValueInfo(HAPWrapperValueInAttribute valueInfo) {
		this.m_valueWrapper = valueInfo;
	}

	public void setValueOfValue(Object value) {		this.setValueInfo(new HAPWrapperValueInAttributeValue(value));	}
	public void setValueOfBrick(HAPBrick brick) {		this.setValueInfo(new HAPWrapperValueInAttributeBrick(brick));	}
	public void setValueOfResourceId(HAPResourceId resourceId) {		this.setValueInfo(new HAPWrapperValueInAttributeReferenceResource(resourceId));	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toStringValue(HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toResourceData(runtimeInfo).toString());
	}
	
	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		dependency.addAll(this.m_valueWrapper.getResourceDependency(runtimeInfo, resourceManager));
	}
}
