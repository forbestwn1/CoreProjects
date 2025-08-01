package com.nosliw.core.application;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPWrapperValueOfDynamic extends HAPWrapperValue{

	@HAPAttribute
	public static final String DYNAMIC = "dynamic";
	
	private HAPValueOfDynamic m_dynamicValue;
	
	public HAPWrapperValueOfDynamic(HAPValueOfDynamic dynamicValue) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_DYNAMIC);
		this.m_dynamicValue = dynamicValue;
	}
	
	@Override
	public Object getValue() {    return this.getDynamicValue();     }
	
	public HAPValueOfDynamic getDynamicValue() {    return this.m_dynamicValue;     }
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DYNAMIC, HAPManagerSerialize.getInstance().toStringValue(this.m_dynamicValue, HAPSerializationFormat.JAVASCRIPT));
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
	}

}
