package com.nosliw.miniapp.instance;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.miniapp.HAPMiniAppResult;
import com.nosliw.miniapp.HAPMiniAppSetting;
import com.nosliw.uiresource.module.HAPInstanceUIModule;

@HAPEntityWithAttribute
public class HAPInstanceMiniAppUIEntry extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String SETTING = "setting";
	
	@HAPAttribute
	public static String APP = "app";
	
	private String m_id;
	
	private Map<String, HAPInstanceUIModule> m_uiModules;
	
	private List<HAPInstanceUIModuleSetting> m_uiModulesSetting;
	
	
	private String m_name;

//	private HAPMiniAppSetting m_setting;
//	private HAPMiniAppResult m_app;

	public HAPInstanceMiniAppUIEntry() {}

	public HAPInstanceMiniAppUIEntry(String id, String name) {
		this.m_id = id;
		this.m_name = name;
	}
	
	public String getId() {  return this.m_id;   }
	public String getName() {  return this.m_name;  }
	public void setId(String id) {  this.m_id = id;  }
	public void setName(String name) {  this.m_name = name;  }
	
	public void setSetting(HAPMiniAppSetting setting) {  this.m_setting = setting;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(SETTING, HAPSerializeManager.getInstance().toStringValue(m_setting, HAPSerializationFormat.JSON));
		jsonMap.put(APP, HAPSerializeManager.getInstance().toStringValue(m_app, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = jsonObj.optString(ID);
		this.m_name = jsonObj.optString(NAME);
		this.m_setting = (HAPMiniAppSetting)HAPSerializeManager.getInstance().buildObject(HAPMiniAppSetting.class.getName(), jsonObj.opt(SETTING), HAPSerializationFormat.JSON);
		this.m_app = (HAPMiniAppResult)HAPSerializeManager.getInstance().buildObject(HAPMiniAppResult.class.getName(), jsonObj.opt(APP), HAPSerializationFormat.JSON);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return this.buildObjectByFullJson(json);
	}
}
