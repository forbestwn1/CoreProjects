package com.nosliw.data.core;

import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;

/**
 * DataWapper is used to create data structure during processing constant in configuration
 * It just parse the information about the data type and store them
 * But for data value part, it store the string representing value in configuration  
 * In addition to that a flag (wapperType) is used to specify the type of the string content (json, literate) 
 */
public class HAPDataWrapper extends HAPDataImp{

	@HAPAttribute
	public static String WAPPER_TYPE = "wapperType";
	
	private HAPSerializationFormat m_wrapperType;
	
	public HAPDataWrapper(){}
	
	public HAPDataWrapper(String strValue){
		this.buildDataObject(strValue, null);
	}
	
	public String getWrapperType(){		return this.m_wrapperType.name();	}
	private void setWrapperType(HAPSerializationFormat wrapperType){ this.m_wrapperType = wrapperType;  }

	public String getContent(){		return (String)this.getValue();	}
	
	@Override
	protected String toStringValueValue(HAPSerializationFormat format) {
		return this.getContent();
	}

	@Override
	Object buildObjectVale(Object value, HAPSerializationFormat format) {
		if(value!=null) return value.toString();
		return null;
	}
	
	@Override
	protected void buildFullJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(WAPPER_TYPE, this.getWrapperType());
	}
	
	/*
	 * transform string to data object, according to the structure of the string, for instance: 
	 * 		json : start with { 
	 * 		literal : #type:categary:value
	 * 		otherwise, treat as simple text
	 */
	public boolean buildDataObject(String text, HAPDataTypeId dataTypeId){
		try {
			if(text==null)  return false;
			
			String token = text.substring(0, 1);
			if(token.equals("{")){
				JSONObject jsonObj = new JSONObject(text);
				String dataTypeIdLiterate = jsonObj.optString(DATATYPEID);
				if(HAPBasicUtility.isStringEmpty(dataTypeIdLiterate)){
					//json
					this.buildObjectByJson(jsonObj);
					this.setDataTypeId(dataTypeId);
				}
				else{
					//full json
					this.buildObjectByFullJson(jsonObj);
				}
				this.setWrapperType(HAPSerializationFormat.JSON);
				return true;
			}
			else if(token.equals("#")){
				//literate
				this.buildObjectByLiterate(text);
				this.setWrapperType(HAPSerializationFormat.LITERATE);
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
}
