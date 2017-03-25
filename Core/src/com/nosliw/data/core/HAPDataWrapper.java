package com.nosliw.data.core;

import org.json.JSONException;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.utils.HAPAttributeConstant;
import com.nosliw.data1.HAPData;
import com.nosliw.data1.HAPDataType;
import com.nosliw.data1.HAPDataTypeInfo;

/**
 * DataWapper is used to create data structure during processing constant in configuration
 * It just parse the information about the data type and store them
 * But for data value part, it store the string representing value in configuration  
 * In addition to that a flag (wapperType) is used to specify the type of the string content (json, literate) 
 */
public class HAPDataWrapper extends HAPDataImp{

	@HAPAttribute
	public static String WAPPER_TYPE = "wapperType";
	
	private String m_wrapperType;
	
	public HAPDataWrapper(String strValue){
		
	}
	
	public String getWrapperType(){
		return this.m_wrapperType;
	}

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
	
	/*
	 * transform string to data object, according to the structure of the string, for instance: 
	 * 		json : start with { 
	 * 		literal : #type:categary:value
	 * 		otherwise, treat as simple text
	 */
	public void parseString(String text, String dataTypeId){
		if(text==null)  return;
		if(text.equals(""))  return this.getStringData(text);
		
		String token = text.substring(0, 1);
		if(token.equals("{")){
			JSONObject jsonObj = new JSONObject(text);
			String dataTypeIdLiterate = jsonObj.optString(DATATYPEID);
			if(HAPBasicUtility.isStringEmpty(dataTypeIdLiterate)){
				//json
				this.buildObjectByJson(jsonObj);
			}
			else{
				//full json
				this.buildObjectByFullJson(jsonObj);
			}
		}
		else if(token.equals("#")){
			//literate
			this.buildObjectByLiterate(text);
		}
		else{
			if(categary!=null && type!=null){
				HAPDataType dataType = this.getDataType(new HAPDataTypeInfo(categary, type));
				return dataType.toData(text, HAPSerializationFormat.TEXT);
			}
			else{
				//simple / text
				return this.getStringData(text);
			}
		}
	}
	
	public HAPData parseJson(JSONObject jsonObj, String categary, String type){
		if(HAPBasicUtility.isStringEmpty(categary)){
			categary = jsonObj.optJSONObject(HAPAttributeConstant.DATA_DATATYPEINFO).optString(HAPAttributeConstant.DATATYPEINFO_CATEGARY);
		}
		if(HAPBasicUtility.isStringEmpty(type)){
			type = jsonObj.optJSONObject(HAPAttributeConstant.DATA_DATATYPEINFO).optString(HAPAttributeConstant.DATATYPEINFO_TYPE);
		}
		Object valueObj = jsonObj.opt(HAPAttributeConstant.DATA_VALUE);
		if(valueObj==null)  valueObj = jsonObj;
		return this.getDataType(new HAPDataTypeInfo(categary, type)).toData(valueObj, HAPSerializationFormat.JSON);
	}
	
}
