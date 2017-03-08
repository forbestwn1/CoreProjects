package com.nosliw.data.core;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public class HAPDataUtility {

	public static HAPData buildDataWrapperJson(JSONObject jsonObj){
		HAPData out = (HAPData)HAPSerializeManager.getInstance().buildObject(HAPDataWrapperJson.class.getName(), jsonObj, HAPSerializationFormat.JSON_FULL); 
		return out;
	}

	public static HAPDataTypeCriteria buildDataTypeCriteriaFromJson(JSONObject jsonObj){
		HAPDataTypeCriteria out = (HAPDataTypeCriteria)HAPSerializeManager.getInstance().buildObject(HAPDataTypeCriteria.class.getName(), jsonObj, HAPSerializationFormat.JSON_FULL); 
		return out;
	}
	
}
