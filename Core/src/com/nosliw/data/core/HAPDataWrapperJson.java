package com.nosliw.data.core;

import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

/**
 * The wrapper class for data 
 * It only parse the data type id part
 * However, store the data value as it is (json structure)
 */
public class HAPDataWrapperJson extends HAPDataImp{

	private Object m_jsonValue;
	
	public HAPDataWrapperJson(JSONObject dataJson){
		buildObjectByFullJson(dataJson);
	}
	
	public HAPDataWrapperJson(HAPDataTypeId dataTypeId, Object value) {
		super(dataTypeId, value);
	}

	@Override
	Object buildObjectVale(Object value, HAPSerializationFormat format) {
		this.m_jsonValue = value;
		return this.m_jsonValue;
	}

	@Override
	protected String toStringValueValue(HAPSerializationFormat format) {
		if(this.m_jsonValue==null)  return null;
		return this.m_jsonValue.toString();
	}
	
	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		try {
			this.buildObjectByFullJson(new JSONObject(literateValue));
			return true;
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
}
