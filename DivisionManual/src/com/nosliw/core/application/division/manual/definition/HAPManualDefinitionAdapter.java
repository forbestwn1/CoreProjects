package com.nosliw.core.application.division.manual.definition;

import java.util.Map;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPManualDefinitionAdapter extends HAPEntityInfoImp{

	public final static String VALUEWRAPPER = "valueWrapper"; 

	private HAPManualDefinitionWrapperValue m_valueWrapper;
	
	public HAPManualDefinitionAdapter(HAPManualDefinitionWrapperValue valueWrapper) {
		this.m_valueWrapper = valueWrapper;
	}

	public HAPManualDefinitionWrapperValue getValueWrapper() {    return this.m_valueWrapper;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEWRAPPER, this.m_valueWrapper.toStringValue(HAPSerializationFormat.JSON));
	}
}
