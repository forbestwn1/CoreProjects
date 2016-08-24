package com.nosliw.common.strvalue.basic;

import java.util.Map;

import com.nosliw.common.serialization.HAPStringableJson;

public abstract class HAPStringableValue extends HAPStringableJson implements HAPResolvable{

	public abstract String getStringableCategary();

	public abstract HAPStringableValue getChild(String name);

	public abstract HAPStringableValue clone();
	
	public abstract boolean isEmpty();
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap, String format) {
		jsonMap.put(HAPAttributeConstant.ATTR_STRINGABLEVALUE_CATEGARY, this.getStringableCategary());
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> typeJsonMap, String format) {}
}
