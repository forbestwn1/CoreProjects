package com.nosliw.core.application.dynamic;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

@HAPEntityWithAttribute
public abstract class HAPInfoDynamic extends HAPEntityInfoImp{

	@HAPAttribute
	public final static String TYPE = "type"; 

	public abstract String getType();

	public abstract HAPInfoDynamic getChild(String childName);
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
	}

	
	public static HAPInfoDynamic parse(Object obj) {
		HAPInfoDynamic out = null;
		
		JSONObject jsonObj = (JSONObject)obj;
		String type = jsonObj.getString(TYPE);
		
		switch(type) {
		case HAPConstantShared.DYNAMICTASK_INFO_TYPE_SET:
			out = new HAPInfoDynamicLeafSet();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
			break;
		case HAPConstantShared.DYNAMICTASK_INFO_TYPE_SIMPLE:
			out = new HAPInfoDynamicLeafSimple();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
			break;
		case HAPConstantShared.DYNAMICTASK_INFO_TYPE_NODE:
			out = new HAPInfoDynamicNode();
			out.buildObject(jsonObj, HAPSerializationFormat.JSON);
			break;
		}
		
		if(out.getName()==null) {
			out.setName(HAPConstantShared.NAME_DEFAULT);
		}
		
		return out;
	}
	
}
