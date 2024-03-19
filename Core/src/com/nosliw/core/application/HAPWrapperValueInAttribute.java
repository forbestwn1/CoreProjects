package com.nosliw.core.application;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public abstract class HAPWrapperValueInAttribute extends HAPExecutableImp{

	@HAPAttribute
	public static final String VALUETYPE = "valueType";
	
	@HAPAttribute
	public static final String VALUE = "value";
	
	private String m_valueType;
	
	public HAPWrapperValueInAttribute(String valueType) {
		this.m_valueType = valueType;
	}
	
	public String getValueType() {     return this.m_valueType;    }

	abstract public Object getValue();
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUETYPE, this.m_valueType);
		jsonMap.put(VALUE, HAPSerializeManager.getInstance().toStringValue(this.getValue(), HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo){
		this.buildJsonMap(jsonMap, typeJsonMap);
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(this.getValue() instanceof HAPExecutable) {
			jsonMap.put(VALUE, ((HAPExecutable)this.getValue()).toResourceData(runtimeInfo).toString());
		}
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		if(this.getValue() instanceof HAPExecutable) {
			dependency.addAll(((HAPExecutable)this.getValue()).getResourceDependency(runtimeInfo, resourceManager));
		}
	}
	
}
