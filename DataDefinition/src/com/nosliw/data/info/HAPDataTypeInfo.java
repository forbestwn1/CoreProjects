package com.nosliw.data.info;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.utils.HAPAttributeConstant;

/*
 * entity store information about a data type information
 */
public class HAPDataTypeInfo implements HAPStringable{

	private String m_categary;
	private String m_type;

	public HAPDataTypeInfo(){}
	
	public HAPDataTypeInfo(String categary, String type){
		this.m_categary = categary;
		this.m_type = type;
	}

	public String getCategary(){return this.m_categary;}
	public String getType(){return this.m_type;	}
	public void setCategary(String categary){ this.m_categary = categary;}
	public void setType(String type){ this.m_type = type;}
	
	public HAPDataTypeInfo cloneDataTypeInfo(){
		return new HAPDataTypeInfo(this.getCategary(), this.getType());
	}
	
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonTypeMap){
		jsonMap.put(HAPAttributeConstant.ATTR_DATATYPEINFO_CATEGARY, this.getCategary());
		jsonMap.put(HAPAttributeConstant.ATTR_DATATYPEINFO_TYPE, this.getType());
		jsonMap.put(HAPAttributeConstant.ATTR_DATATYPEINFO_KEY, this.getKey());
	}
	
	@Override
	public String toStringValue(String format){
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class> jsonTypeMap = new LinkedHashMap<String, Class>();
		this.buildJsonMap(jsonMap, jsonTypeMap);
		return HAPJsonUtility.getMapJson(jsonMap, jsonTypeMap);
	}

	//get unique key
	public String getKey(){return this.toString();}
	
	@Override
	public String toString(){	return HAPNamingConversionUtility.cascadePart(this.m_type, this.m_categary);	}
	
	public static HAPDataTypeInfo parse(JSONObject jsonObj){
		String type = jsonObj.optString(HAPAttributeConstant.ATTR_DATATYPEINFO_TYPE);
		String categary = jsonObj.optString(HAPAttributeConstant.ATTR_DATATYPEINFO_CATEGARY);
		return new HAPDataTypeInfo(categary, type);
	}
	
	//get DataTypeInfo from string  (type:categary)
	public static HAPDataTypeInfo parseDataTypeInfo(String infoStr, HAPDataTypeInfo backup){
		if(HAPBasicUtility.isStringEmpty(infoStr))	return backup;
		else{
	    	String[] parts = HAPNamingConversionUtility.parsePartlInfos(infoStr);
			String type = parts[0];
			String categary = HAPDataTypeManager.DEFAULT_TYPE;
			if(parts.length>=2)   categary = parts[1];
			return new HAPDataTypeInfo(categary, type);
		}
	}

	public static HAPDataTypeInfo parseDataTypeInfo(String infoStr){
		return parseDataTypeInfo(infoStr, null);
	}
	
	@Override
	public boolean equals(Object data){
		if(data instanceof HAPDataTypeInfo){
			HAPDataTypeInfo type = (HAPDataTypeInfo)data;
			return HAPBasicUtility.isEquals(type.getCategary(), this.getCategary()) && 
					HAPBasicUtility.isEquals(type.getType(), this.getType()); 
		}
		return false;
	}
}
