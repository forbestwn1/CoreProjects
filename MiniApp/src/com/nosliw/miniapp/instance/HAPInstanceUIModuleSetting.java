package com.nosliw.miniapp.instance;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.data.core.HAPDataWrapper;

@HAPEntityWithAttribute
public class HAPInstanceUIModuleSetting  extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String SETTINGS = "settings";

	private String m_id;
	
	private String m_name;
	
	private Map<String, Map<String, Object>> m_settings;

	public HAPInstanceUIModuleSetting() {
		this.m_settings = new LinkedHashMap<String, Map<String, Object>>();
	}
	
	public void setId(String id) {   this.m_id = id;  }
	public void setName(String name) {  this.m_name = name;   }
	
	public void addSettingData(String module, String settingName, Object data) {
		Map<String, Object> moduleSettingData = this.m_settings.get(module);
		if(moduleSettingData==null) {
			moduleSettingData = new LinkedHashMap<String, Object>();
			this.m_settings.put(module, moduleSettingData);
		}
		moduleSettingData.put(settingName, data);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(SETTINGS, HAPJsonUtility.buildJson(this.m_settings, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = (String)jsonObj.opt(ID);
		this.m_name = (String)jsonObj.opt(NAME);
		this.m_settings = HAPSerializeUtility.buildMapFromJsonObject(HAPDataWrapper.class.getName(), jsonObj.optJSONObject(SETTINGS));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}

}
