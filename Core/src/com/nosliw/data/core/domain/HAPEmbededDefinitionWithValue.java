package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

@HAPEntityWithAttribute
public class HAPEmbededDefinitionWithValue extends HAPEmbededDefinition{

	@HAPAttribute
	public static String VALUE = "value";

	public HAPEmbededDefinitionWithValue() {}
	
	public HAPEmbededDefinitionWithValue(Object value) {
		super(value, null, false);
	}
	
	@Override
	public HAPEmbededDefinitionWithValue cloneEmbeded() {
		HAPEmbededDefinitionWithValue out = new HAPEmbededDefinitionWithValue(this.getValue());
		this.cloneToEmbeded(out);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE,  HAPSerializeManager.getInstance().toStringValue(this.getValue(), HAPSerializationFormat.JSON));
	}
}
