package com.nosliw.core.application;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPWithResourceDependency;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPAdapter extends HAPEntityInfoImp implements HAPWithResourceDependency{

	@HAPAttribute
	public static final String VALUEWRAPPER = "valueWrapper";
	
	private HAPWrapperValue m_valueWrapper;
	
	public HAPAdapter(HAPWrapperValue valueWrapper) {
		this.m_valueWrapper = valueWrapper;
	}
	
	public HAPWrapperValue getValueWrapper() {
		return this.m_valueWrapper;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_valueWrapper!=null) {
			jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toStringValue(HAPSerializationFormat.JSON));
		}
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toStringValue(HAPSerializationFormat.JAVASCRIPT));
	}

	@Override
	public void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo) {
		this.m_valueWrapper.buildResourceDependency(dependency, runtimeInfo);
	}
}
