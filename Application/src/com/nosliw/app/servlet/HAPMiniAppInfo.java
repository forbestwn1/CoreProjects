package com.nosliw.app.servlet;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataWrapper;

@HAPEntityWithAttribute
public class HAPMiniAppInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String SETTING = "setting";
	
	@HAPAttribute
	public static String APP = "app";
	
	private String m_id;
	
	private String m_name;

	private HAPMiniAppSetting m_setting;
	
	private HAPMiniApp m_app;

	public HAPMiniAppInfo() {}

	public HAPMiniAppInfo(String id, String name) {
		this.m_id = id;
		this.m_name = name;
	}
	
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
		this.m_app = (HAPMiniApp)HAPSerializeManager.getInstance().buildObject(HAPMiniApp.class.getName(), jsonObj.opt(APP), HAPSerializationFormat.JSON);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return this.buildObjectByFullJson(json);
	}
}
