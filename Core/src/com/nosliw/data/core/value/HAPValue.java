package com.nosliw.data.core.value;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPValue extends HAPSerializableImp{

	@HAPAttribute
	public static String VALUE = "value";

	@HAPAttribute
	public static String TYPE = "type";

	private Object m_value;
	
	private String m_type;
	
	public Object getValue() {   return this.m_value;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, HAPJsonUtility.buildJson(this.m_value, HAPSerializationFormat.JSON));
		jsonMap.put(TYPE, this.m_type);
	}
	
	@Override
	public boolean buildObjectByJson(Object value) {
		JSONObject valueObj = (JSONObject)value;
		super.buildObjectByJson(valueObj);
		this.m_type = (String)valueObj.opt(TYPE);
		this.m_value = valueObj.opt(VALUE);
		return true;
	}
}
