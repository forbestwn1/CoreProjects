package com.nosliw.core.application.division.manual.core.definition1;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPValueOfDynamic;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperValue;

public class HAPManualDefinitionWrapperValueDynamic extends HAPManualDefinitionWrapperValue{

	@HAPAttribute
	public static final String DYNAMIC = "dynamic";
	
	private HAPValueOfDynamic m_dynamicValue;
	
	public HAPManualDefinitionWrapperValueDynamic(HAPValueOfDynamic dynamicValue) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_DYNAMIC);
		this.m_dynamicValue = dynamicValue;
	}
	
	public HAPValueOfDynamic getDynamicValue() {    return this.m_dynamicValue;     }
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJSJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(DYNAMIC, HAPManagerSerialize.getInstance().toStringValue(this.m_dynamicValue, HAPSerializationFormat.JAVASCRIPT));
	}
}
