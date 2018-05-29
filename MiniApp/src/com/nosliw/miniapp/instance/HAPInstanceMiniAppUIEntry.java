package com.nosliw.miniapp.instance;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.serialization.HAPSerializeUtility;
import com.nosliw.uiresource.module.HAPInstanceUIModule;

@HAPEntityWithAttribute
public class HAPInstanceMiniAppUIEntry extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String UIMODULES = "uiModules";
	
	@HAPAttribute
	public static String UIMODULESETTINGS = "uiModuleSettings";
	
	private String m_id;
	
	private Map<String, HAPInstanceUIModule> m_uiModules;
	
	private List<HAPInstanceUIModuleSetting> m_uiModuleSettings;
	
	public HAPInstanceMiniAppUIEntry() {}

	public HAPInstanceMiniAppUIEntry(String id) {
		this.m_id = id;
	}
	
	public String getId() {  return this.m_id;   }
	public void setId(String id) {  this.m_id = id;  }
	
	public void addUIModuleInstance(String name, HAPInstanceUIModule uiModuleInstance) {
		this.m_uiModules.put(name, uiModuleInstance);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(UIMODULES, HAPSerializeManager.getInstance().toStringValue(this.m_uiModules, HAPSerializationFormat.JSON));
		jsonMap.put(UIMODULESETTINGS, HAPSerializeManager.getInstance().toStringValue(this.m_uiModuleSettings, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = jsonObj.optString(ID);
		this.m_uiModules =  HAPSerializeUtility.buildMapFromJsonObject(HAPInstanceUIModule.class.getName(), jsonObj.optJSONObject(UIMODULES));
		this.m_uiModuleSettings =  HAPSerializeUtility.buildListFromJsonArray(HAPInstanceUIModuleSetting.class.getName(), jsonObj.optJSONArray(UIMODULESETTINGS));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}
}
