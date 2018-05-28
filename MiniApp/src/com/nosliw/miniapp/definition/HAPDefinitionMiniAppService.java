package com.nosliw.miniapp.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;

@HAPEntityWithAttribute
public interface HAPDefinitionMiniAppService {

	@HAPAttribute
	public static final String TYPE = "type";
	
	String getType();
	
	public static Map<String, Class> m_serviceClasses = new LinkedHashMap<String, Class>();
	
	public static HAPDefinitionMiniAppService buildObject(Object obj) {
		JSONObject jsonObj = (JSONObject)obj;
		String type = (String)jsonObj.get(TYPE);
		HAPDefinitionMiniAppService out = (HAPDefinitionMiniAppService)HAPSerializeManager.getInstance().buildObject(m_serviceClasses.get(type).getName(), jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
}
