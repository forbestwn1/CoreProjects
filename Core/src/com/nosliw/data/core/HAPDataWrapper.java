package com.nosliw.data.core;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;

/**
 * DataWapper is used to create data structure during processing constant in configuration
 * It just parse the information about the data type and store them
 * But for data value part, it store the string representing value in configuration  
 * In addition to that a flag (wapperType) is used to specify the type of the string content (json, literate) 
 */
public class HAPDataWrapper  extends HAPSerializableImp implements HAPData{

	@HAPAttribute
	public static String VALUEFORMAT = "valueFormat";

	public final static String TOKEN_LITERATE = "#";
	public final static String TOKEN_JSON = "{";
	
	//data type
	protected HAPDataTypeId m_dataTypeId;
	//any object that can represent data value (json, literate)
	protected Object m_value;

	
	private HAPSerializationFormat m_valueFormat;
	
	public HAPDataWrapper(){
	}
	
	public HAPDataWrapper(String strValue){
		this.buildObjectByLiterate(strValue);
	}

	public HAPDataWrapper(HAPDataTypeId dataTypeId, Object value){
		this.m_dataTypeId = dataTypeId;
		this.m_value = value;
	}
	
	@Override
	public HAPDataTypeId getDataTypeId() {		return this.m_dataTypeId;	}
	protected void setDataTypeId(HAPDataTypeId dataTypeId){  this.m_dataTypeId = dataTypeId;  }  
	
	@Override
	public Object getValue() {		return this.m_value;	}
	
	public String getValueFormat(){		return this.m_valueFormat.name();	}
	private void setValueFormat(HAPSerializationFormat valueFormat){ this.m_valueFormat = valueFormat;  }

	public String getContent(){		return (String)this.getValue();	}
	
	
	/*
	 * transform string to data object
	 * the string can be in different format: 
	 * 		json : start with { 
	 * 		literal : #type:categary:value
	 * 		otherwise, treat as simple text
	 */
	@Override
	public boolean buildObjectByLiterate(String text){
		try {
			if(text==null)  return false;
			
			String token = text.substring(0, 1);
			if(token.equals(TOKEN_JSON)){
				//json
				JSONObject jsonObj = new JSONObject(text);
				this.setValueFormat(HAPSerializationFormat.JSON);
				this.buildObjectByFullJson(jsonObj);
				return true;
			}
			else if(token.equals(TOKEN_LITERATE)){
				//literate
				//for literate structure, the value should also be literate, 
				//it is not case for json structure 
				this.setValueFormat(HAPSerializationFormat.LITERATE);
				//parse literate to get data type and value parts
				String[] parts = HAPNamingConversionUtility.parseDetails(text.substring(1));
				if(parts.length<2)   return false;
				this.m_dataTypeId = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), parts[0], HAPSerializationFormat.LITERATE);
				this.m_value = parts[1];
				return true;
			}
			else{
				return false;
			}
		} catch (JSONException e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		//data type id
		String dataTypeIdLiterate = jsonObj.optString(DATATYPEID);
		if(HAPBasicUtility.isStringEmpty(dataTypeIdLiterate))  return false;
		this.m_dataTypeId = (HAPDataTypeId)HAPSerializeManager.getInstance().buildObject(HAPDataTypeId.class.getName(), dataTypeIdLiterate, HAPSerializationFormat.LITERATE);

		//value format
		Object valueFormat = jsonObj.opt(VALUEFORMAT);
		if(valueFormat==null)   this.m_valueFormat = HAPSerializationFormat.JSON;
		else  this.m_valueFormat = HAPSerializationFormat.valueOf((String)valueFormat);

		//value
		this.m_value = jsonObj.opt(VALUE);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		
		return this.buildObjectByFullJson(json);
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DATATYPEID, this.m_dataTypeId.toStringValue(HAPSerializationFormat.LITERATE));
		jsonMap.put(VALUEFORMAT, this.getValueFormat());
		if(this.m_value instanceof String || this.m_value instanceof Boolean || this.m_value instanceof Integer || this.m_value instanceof Double){
			jsonMap.put(VALUE, this.m_value+"");
			typeJsonMap.put(VALUE, this.m_value.getClass());
		}
		else{
			jsonMap.put(VALUE, this.m_value+"");
		}
	}

	@Override
	protected String buildLiterate(){
		return this.toStringValue(HAPSerializationFormat.JSON);
//		return TOKEN_LITERATE + HAPNamingConversionUtility.cascadeDetail(this.m_dataTypeId.toStringValue(HAPSerializationFormat.LITERATE), this.m_value+"");
	}
}
