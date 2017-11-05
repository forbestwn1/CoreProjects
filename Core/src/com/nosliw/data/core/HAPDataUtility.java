package com.nosliw.data.core;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPDataUtility {

	public static HAPDataWrapper buildDataWrapper(String strValue){
		HAPDataWrapper wrapper = new HAPDataWrapper();
		if(wrapper.buildObjectByLiterate(strValue))  return wrapper;
		return null;
	}

	public static HAPDataWrapper buildDataWrapperFromJson(JSONObject jsonObj){
		HAPDataWrapper wrapper = new HAPDataWrapper();
		boolean result = wrapper.buildObjectByJson(jsonObj);
		if(result)   return wrapper;
		else return null;
	}
	
	public static HAPDataTypeCriteria buildDataTypeCriteriaFromJson(JSONObject jsonObj){
		HAPDataTypeCriteria out = (HAPDataTypeCriteria)HAPSerializeManager.getInstance().buildObject(HAPDataTypeCriteria.class.getName(), jsonObj, HAPSerializationFormat.JSON_FULL); 
		return out;
	}
	
	public static boolean isNormalDataOpration(HAPOperation operation){
		String type = operation.getType();
		return type==null || HAPConstant.DATAOPERATION_TYPE_NORMAL.equals(type);
	}
	
	
}
