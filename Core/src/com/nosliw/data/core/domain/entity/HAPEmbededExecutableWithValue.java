package com.nosliw.data.core.domain.entity;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public class HAPEmbededExecutableWithValue extends HAPEmbededExecutable{

	@HAPAttribute
	public static String VALUE = "value";

	public HAPEmbededExecutableWithValue() {}
	
	public HAPEmbededExecutableWithValue(Object value) {
		super(value, null, false);
	}
	
	@Override
	public String getType() {  return HAPConstantShared.EMBEDED_TYPE_EXECUTABLE_VALUE;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, HAPSerializeManager.getInstance().toStringValue(this.getValue(), HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPEmbededExecutableWithValue cloneEmbeded() {
		HAPEmbededExecutableWithValue out = new HAPEmbededExecutableWithValue();
		this.cloneToEmbeded(out);
		return out;
	}
}
