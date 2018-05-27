package com.nosliw.miniapp;

import java.util.LinkedHashMap;
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
public class HAPMiniAppSetting extends HAPSerializableImp{

	@HAPAttribute
	public static String PARMS = "parms";

	@HAPAttribute
	public static String UIMODULE = "uiModule";

	private Map<String, Object> m_parms;

	private HAPUIModule m_uiModule;
	
	public HAPMiniAppSetting() {
		this.m_parms = new LinkedHashMap<String, Object>();
	}
	
	public void setUIModule(HAPUIModule uiModule) { this.m_uiModule = uiModule;  }
	
	public void addParm(String name, Object appData) {  this.m_parms.put(name, appData);   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(PARMS, HAPJsonUtility.buildJson(this.m_parms, HAPSerializationFormat.JSON));
		jsonMap.put(UIMODULE, HAPJsonUtility.buildJson(this.m_uiModule, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_parms = HAPSerializeUtility.buildMapFromJsonObject(HAPDataWrapper.class.getName(), jsonObj.optJSONObject(PARMS));
		this.m_uiModule = (HAPUIModule)HAPSerializeManager.getInstance().buildObject(HAPUIModule.class.getName(), jsonObj.optJSONObject(UIMODULE), HAPSerializationFormat.JSON);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		return this.buildObjectByFullJson(json);
	}
}
