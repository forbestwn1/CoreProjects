package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

public abstract class HAPOperandImp  extends HAPSerializableImp implements HAPOperand{

	public static final String TYPE = "type"; 
	
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
	}
}
