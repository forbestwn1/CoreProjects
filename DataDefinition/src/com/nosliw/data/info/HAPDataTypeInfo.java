package com.nosliw.data.info;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.pattern.HAPNamingConversionUtility;
import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.strvalue.basic.HAPStringableValueEntity;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.data.utils.HAPAttributeConstant;

/*
 * entity store information about a data type information
 */
public class HAPDataTypeInfo extends HAPStringableValueEntity{

	public static String ENTITY_PROPERTY_CATEGARY = "categary";
	public static String ENTITY_PROPERTY_TYPE = "type";
	public static String ENTITY_PROPERTY_KEY = "key";

	public HAPDataTypeInfo(){}
	
	public HAPDataTypeInfo(String categary, String type){
		this.setCategary(categary);
		this.setType(type);
	}

	public String getCategary(){return this.getBasicAncestorValueString(ENTITY_PROPERTY_CATEGARY);}
	public String getType(){return this.getBasicAncestorValueString(ENTITY_PROPERTY_TYPE);	}
	public void setCategary(String categary){ this.updateBasicChild(ENTITY_PROPERTY_CATEGARY, categary, HAPConstant.CONS_STRINGABLE_BASICVALUETYPE_STRING);}
	public void setType(String type){ this.updateBasicChild(ENTITY_PROPERTY_TYPE, type, HAPConstant.CONS_STRINGABLE_BASICVALUETYPE_STRING);}
	
	public HAPDataTypeInfo cloneDataTypeInfo(){
		return new HAPDataTypeInfo(this.getCategary(), this.getType());
	}
	
	//get unique key
	public String getKey(){return this.toString();}
	
	@Override
	public String toString(){	return HAPNamingConversionUtility.cascadePart(this.getType(), this.getCategary());	}
	
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
