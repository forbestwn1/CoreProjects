package com.nosliw.miniapp.definition;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;

@HAPEntityWithAttribute
public class HAPDefinitionMiniAppUIEntry  extends HAPSerializableImp{

	@HAPAttribute
	public static final String UIMODULEENTRIES = "uiModuleEntries";

	private Map<String, HAPDefinitionMiniAppModuleEntry> m_uiModuleEntries;

	public Map<String, HAPDefinitionMiniAppModuleEntry> getUIModuleEntries(){  return this.m_uiModuleEntries;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(UIMODULEENTRIES, HAPJsonUtility.buildJson(this.m_uiModuleEntries, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_uiModuleEntries =  HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionMiniAppModuleEntry.class.getName(), jsonObj.optJSONObject(UIMODULEENTRIES));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}
	
}
