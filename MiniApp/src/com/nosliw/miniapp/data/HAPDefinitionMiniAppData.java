package com.nosliw.miniapp.data;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstant;

public abstract class HAPDefinitionMiniAppData extends HAPSerializableImp{

	public abstract String getType();
	
	@HAPAttribute
	public static final String TYPE = "type";
	
	public static Map<String, Class> m_dataDefClasses = new LinkedHashMap<String, Class>();
	
	static {
		m_dataDefClasses.put(HAPConstant.MINIAPPDATA_TYPE_SETTING, HAPDefinitionMiniAppDataSetting.class);
	}
	
	public static HAPDefinitionMiniAppData buildObject(Object obj) {
		JSONObject jsonObj = (JSONObject)obj;
		String type = (String)jsonObj.get(TYPE);
		HAPDefinitionMiniAppData out = (HAPDefinitionMiniAppData)HAPSerializeManager.getInstance().buildObject(m_dataDefClasses.get(type).getName(), jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(TYPE, this.getType());
	}
	
	
}
