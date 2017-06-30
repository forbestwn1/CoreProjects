package com.nosliw.data.core;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;

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
	protected void setDataTypeId(HAPDataTypeId dataTypeId){  this.m_dataTypeId = dataTypeId;  }  
	
	@Override
	public Object getValue() {		return this.m_value;	}

	
	abstract protected String toStringValueValue(HAPSerializationFormat format);
	abstract Object buildObjectVale(Object value, HAPSerializationFormat format);

	@Override
	protected boolean buildObjectByLiterate(String literateValue){
		String[] parts = HAPNamingConversionUtility.parseDetails(literateValue);
		if(parts.length<2)   return false;
		this.m_dataTypeId = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), parts[0], HAPSerializationFormat.LITERATE);
		this.m_value = this.buildObjectVale(parts[1], HAPSerializationFormat.LITERATE);
		return true;
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		String dataTypeIdLiterate = jsonObj.optString(DATATYPEID);
		if(HAPBasicUtility.isStringEmpty(dataTypeIdLiterate))  return false;
		this.m_dataTypeId = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), dataTypeIdLiterate, HAPSerializationFormat.LITERATE);

		Object value = jsonObj.opt(VALUE);
		this.m_value = this.buildObjectVale(value, HAPSerializationFormat.JSON_FULL);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		
		return this.buildObjectByFullJson(json);
	}

	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DATATYPEID, this.m_dataTypeId.toStringValue(HAPSerializationFormat.LITERATE));
		this.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(VALUE, this.toStringValueValue(HAPSerializationFormat.JSON));
	}

	
	@Override
	protected String buildJson(){ return this.toStringValue(HAPSerializationFormat.JSON_FULL); }

	@Override
	protected String buildLiterate(){
		return HAPNamingConversionUtility.cascadeDetail(this.m_dataTypeId.toStringValue(HAPSerializationFormat.LITERATE), this.toStringValueValue(HAPSerializationFormat.LITERATE));
	}
	
}
