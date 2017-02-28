package com.nosliw.data.core;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

public abstract class HAPDataImp extends HAPSerializableImp implements HAPData{

	protected HAPDataTypeId m_dataTypeId;
	protected Object m_value;
	
	public HAPDataImp(){}
	
	public HAPDataImp(HAPDataTypeId dataTypeId, Object value){
		this.m_dataTypeId = dataTypeId;
		this.m_value = value;
	}
	
	@Override
	public HAPDataTypeId getDataTypeId() {		return this.m_dataTypeId;	}

	@Override
	public Object getValue() {		return this.m_value;	}

	
	abstract protected String toJsonValue();
	abstract Object buildObjectVale(Object value, HAPSerializationFormat format);
	
	@Override
	protected void buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		String dataTypeIdLiterate = jsonObj.optString(DATATYPEID);
		this.m_dataTypeId = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), dataTypeIdLiterate, HAPSerializationFormat.LITERATE);

		Object value = jsonObj.opt(VALUE);
		this.m_value = this.buildObjectVale(value, HAPSerializationFormat.JSON_FULL);
	}

	@Override
	protected void buildObjectByJson(Object json){		this.buildObjectByFullJson(json);	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DATATYPEID, this.m_dataTypeId.toStringValue(HAPSerializationFormat.LITERATE));
		jsonMap.put(VALUE, this.toJsonValue());
	}

	@Override
	protected String buildLiterate(){  return this.toStringValue(HAPSerializationFormat.JSON_FULL); }
	
	@Override
	protected String buildJson(){ return this.toStringValue(HAPSerializationFormat.JSON_FULL); }
}
