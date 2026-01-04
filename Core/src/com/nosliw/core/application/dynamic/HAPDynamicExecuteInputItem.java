package com.nosliw.core.application.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public abstract class HAPDynamicExecuteInputItem extends HAPEntityInfoImp{

	@HAPAttribute
	public final static String TYPE = "type"; 

	public abstract String getType();

	public static HAPDynamicExecuteInputItem parse(Object obj) {
		HAPDynamicExecuteInputItem out = null;
		JSONObject jsonObj = (JSONObject)obj;
		Object typeObj = jsonObj.opt(TYPE);
		String type = typeObj!=null? (String)typeObj : HAPConstantShared.DYNAMICTASK_REF_TYPE_SINGLE;
		switch(type) {
		case HAPConstantShared.DYNAMICTASK_REF_TYPE_SINGLE:
			out = new HAPDynamicExecuteInputItemSingle();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
			break;
		case HAPConstantShared.DYNAMICTASK_REF_TYPE_MULTIPLE:
			out = new HAPDynamicExecuteInputItemMultiple();
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
