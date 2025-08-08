package com.nosliw.core.application.common.structure22;

import java.util.Map;

import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPValueStructureImp extends HAPStructureImp implements HAPValueStructure{

	private Object m_initValue;
	
	public HAPValueStructureImp() {	}

	@Override
	public void setInitValue(Object initValue) {	this.m_initValue = initValue; 	}
	@Override
	public Object getInitValue() {    return this.m_initValue;     } 
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INITVALUE, HAPManagerSerialize.getInstance().toStringValue(this.m_initValue, HAPSerializationFormat.JSON));
	}
}
