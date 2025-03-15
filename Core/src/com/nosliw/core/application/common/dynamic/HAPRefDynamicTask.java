package com.nosliw.core.application.common.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public abstract class HAPRefDynamicTask extends HAPSerializableImp{

	@HAPAttribute
	public final static String TYPE = "type"; 

	public abstract String getType();

	public static HAPRefDynamicTask parse(Object obj) {
		HAPRefDynamicTask out = null;
		JSONObject jsonObj = (JSONObject)obj;
		Object typeObj = jsonObj.opt(TYPE);
		String type = typeObj!=null? (String)typeObj : HAPConstantShared.DYNAMICTASK_REF_TYPE_SIMPLE;
		switch(type) {
		case HAPConstantShared.DYNAMICTASK_REF_TYPE_SIMPLE:
			out = new HAPRefDynamicTaskSimple();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
			break;
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
	}
	
}
