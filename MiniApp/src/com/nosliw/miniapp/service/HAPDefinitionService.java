package com.nosliw.miniapp.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;

@HAPEntityWithAttribute
public abstract class HAPDefinitionService extends HAPSerializableImp{

	@HAPAttribute
	public static final String TYPE = "type";
	
	abstract String getType();
	
	public static Map<String, Class> m_serviceClasses = new LinkedHashMap<String, Class>();
	
	static {
		m_serviceClasses.put(HAPConstant.MINIAPPSERVICE_TYPE_SERVICE, HAPDefinitionServiceService.class);
	}
	
	public static HAPDefinitionService buildObject(Object obj) {
		JSONObject jsonObj = (JSONObject)obj;
		String type = (String)jsonObj.get(TYPE);
		HAPDefinitionService out = (HAPDefinitionService)HAPSerializeManager.getInstance().buildObject(m_serviceClasses.get(type).getName(), jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
	}

	@Override
	protected boolean buildObjectByFullJson(Object json){
		return true;
	}
}
