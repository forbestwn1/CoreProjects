package com.nosliw.data1;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.utils.HAPAttributeConstant;

/*
 * data type info with version number information
 */
public class HAPDataTypeInfoWithVersion extends HAPDataTypeInfo{
	private int m_version = -1;

	public HAPDataTypeInfoWithVersion(String categary, String type, int versionNumber){
		super(categary, type);
		if(versionNumber>0)		this.m_version = versionNumber;
	}

	public HAPDataTypeInfoWithVersion(String categary, String type){
		super(categary, type);
	}
	
	public int getVersionNumber(){return this.m_version;}
	
	public HAPDataTypeInfoWithVersion cloneDataTypeInfo(){
		return new HAPDataTypeInfoWithVersion(this.getCategary(), this.getType(), this.getVersionNumber());
	}
	
	public static HAPDataTypeInfoWithVersion parseDataTypeInfo(String infoStr){
		return parseDataTypeInfo(infoStr, null);
	}
	
	//get DataTypeInfo from string  (type:categary)
	public static HAPDataTypeInfoWithVersion parseDataTypeInfo(String infoStr, HAPDataTypeInfoWithVersion backup){
		if(HAPBasicUtility.isStringEmpty(infoStr))	return backup;
		else{
	    	String[] parts = HAPNamingConversionUtility.parsePartls(infoStr);
			String type = parts[0];
			String categary = HAPDataTypeManager.DEFAULT_TYPE;
			if(parts.length>=2)   categary = parts[1];
			int version = -1;
			if(parts.length>=3)   version = Integer.parseInt(parts[1]);
			return new HAPDataTypeInfoWithVersion(categary, type, version);
		}
	}
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class> jsonTypeMap, String format){
		super.buildJsonMap(jsonMap, jsonTypeMap, format);
		jsonMap.put(HAPAttributeConstant.DATATYPEINFO_VERSION, this.getVersionNumber()+"");
		jsonTypeMap.put(HAPAttributeConstant.DATATYPEINFO_VERSION, Integer.class);
	}

	public static HAPDataTypeInfoWithVersion parse(JSONObject jsonObj){
		String type = jsonObj.optString(HAPAttributeConstant.DATATYPEINFO_TYPE);
		String categary = jsonObj.optString(HAPAttributeConstant.DATATYPEINFO_CATEGARY);
		int version = jsonObj.optInt(HAPAttributeConstant.DATATYPEINFO_VERSION);
		return new HAPDataTypeInfoWithVersion(categary, type, version);
	}
	
	public HAPDataTypeInfo toDataTypeInfo(){	return new HAPDataTypeInfo(this.getCategary(), this.getType());	}
	
	@Override
	public boolean equals(Object data){
		if(data instanceof HAPDataTypeInfoWithVersion){
			HAPDataTypeInfoWithVersion type = (HAPDataTypeInfoWithVersion)data;
			//check super.equal logic
			if(!super.equals(type))  return false;
			//check version
			return	type.getVersionNumber()==type.getVersionNumber(); 
		}
		return false;
	}
	
	public boolean equalsWithoutVersion(Object data){
		if(data instanceof HAPDataTypeInfo){
			HAPDataTypeInfo type = (HAPDataTypeInfo)data;
			//check super.equal logic
			if(!super.equals(type))  return false;
		}
		return false;
	}
}
