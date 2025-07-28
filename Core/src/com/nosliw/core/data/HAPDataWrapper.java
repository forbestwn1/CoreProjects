package com.nosliw.core.data;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.common.utils.HAPUtilityNamingConversion;

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

	public final static String SEPERATOR_DATATYPE = "___";
	
	//data type
	protected HAPDataTypeId m_dataTypeId;
	//any object that can represent data value (json, literate)
	protected Object m_value;

	protected HAPInfo m_info;
	
	private HAPSerializationFormat m_valueFormat;
	
	public HAPDataWrapper(){
		this.m_info = new HAPInfoImpSimple();
	}
	
	public HAPDataWrapper(String strValue){
		this();
		this.buildObjectByLiterate(strValue);
	}

	public HAPDataWrapper(HAPDataTypeId dataTypeId, Object value){
		this();
		this.m_dataTypeId = dataTypeId;
		this.m_value = value;
	}
	
	@Override
	public HAPDataTypeId getDataTypeId() {		return this.m_dataTypeId;	}
	protected void setDataTypeId(HAPDataTypeId dataTypeId){  this.m_dataTypeId = dataTypeId;  }  
	
	@Override
	public Object getValue() {		return this.m_value;	}
	
	@Override
	public HAPInfo getInfo() {  return this.m_info;  }

	public String getValueFormat(){
		if(this.m_valueFormat==null){
			this.m_valueFormat = HAPSerializationFormat.JSON;
		}
		return this.m_valueFormat.name();	
	}
	private void setValueFormat(HAPSerializationFormat valueFormat){ this.m_valueFormat = valueFormat;  }

	public String getContent(){		return (String)this.getValue();	}
	
	
	/*
	 * transform string to data object
	 * the string can be in different format: 
	 * 		json : start with { 
	 * 		literal : #type:value
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
				return this.buildObjectByFullJson(jsonObj);
			}
			else if(token.equals(TOKEN_LITERATE)){
				//literate
				//for literate structure, the value should also be literate, 
				//it is not case for json structure 
				this.setValueFormat(HAPSerializationFormat.LITERATE);
				//parse literate to get data type and value parts
				String[] parts = HAPUtilityNamingConversion.splitTextByComponents(text.substring(TOKEN_LITERATE.length()), SEPERATOR_DATATYPE);
				if(parts.length<2)   return false;
				this.m_dataTypeId = (HAPDataTypeId)HAPManagerSerialize.getInstance().buildObject(HAPDataTypeId.class.getName(), parts[0], HAPSerializationFormat.LITERATE);
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
		if(HAPUtilityBasic.isStringEmpty(dataTypeIdLiterate))  return false;
		this.m_dataTypeId = (HAPDataTypeId)HAPManagerSerialize.getInstance().buildObject(HAPDataTypeId.class.getName(), dataTypeIdLiterate, HAPSerializationFormat.LITERATE);

		//value format
		Object valueFormat = jsonObj.opt(VALUEFORMAT);
		if(valueFormat==null)   this.m_valueFormat = HAPSerializationFormat.JSON;
		else  this.m_valueFormat = HAPSerializationFormat.valueOf((String)valueFormat);

		//value
		this.m_value = jsonObj.opt(VALUE);
		
		//info
		JSONObject infoObj = jsonObj.optJSONObject(INFO);
		if(infoObj!=null) {
			this.m_info.buildObject(infoObj, HAPSerializationFormat.JSON);
		}
		
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		
		return this.buildObjectByFullJson(json);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(DATATYPEID, this.m_dataTypeId.toStringValue(HAPSerializationFormat.LITERATE));
		jsonMap.put(VALUEFORMAT, this.getValueFormat());
		
		HAPUtilityJson.buildJsonMap(VALUE, this.m_value, jsonMap, typeJsonMap, HAPSerializationFormat.JSON);
		
//		if(this.m_value instanceof String || this.m_value instanceof Boolean || this.m_value instanceof Integer || this.m_value instanceof Double){
//			jsonMap.put(VALUE, this.m_value+"");
//			typeJsonMap.put(VALUE, this.m_value.getClass());
//		}
//		else{
//			jsonMap.put(VALUE, this.m_value+"");
//		}

		jsonMap.put(INFO, HAPUtilityJson.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}

	@Override
	protected String buildLiterate(){
		return this.toStringValue(HAPSerializationFormat.JSON);
//		return TOKEN_LITERATE + HAPNamingConversionUtility.cascadeDetail(this.m_dataTypeId.toStringValue(HAPSerializationFormat.LITERATE), this.m_value+"");
	}
	
	@Override
	public boolean equals(Object o){
		boolean out = false;
		if(o instanceof HAPData){
			HAPData data = (HAPData)o;
			if(this.getDataTypeId().equals(data.getDataTypeId())){
				if(this.getValue().equals(data.getValue())){
					out = true;
				}
			}
		}
		return out;
	}

	@Override
	public HAPData cloneData() {
		HAPDataWrapper out = new HAPDataWrapper();
		out.m_dataTypeId = this.m_dataTypeId.clone();
		out.m_value = this.m_value;
		out.m_valueFormat = this.m_valueFormat;
		out.m_info = this.m_info.cloneInfo();
		return out;
	}

}
