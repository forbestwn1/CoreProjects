package com.nosliw.miniapp.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeUtility;

@HAPEntityWithAttribute
public class HAPDefinitionMiniAppEntry  extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static final String MODULE = "module";

	//all modules in this entry
	private Map<String, HAPDefinitionMiniAppModule> m_modules;
	
	public HAPDefinitionMiniAppEntry() {
		this.m_modules = new LinkedHashMap<String, HAPDefinitionMiniAppModule>();
	}
	
	public Map<String, HAPDefinitionMiniAppModule> getModules(){  return this.m_modules;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MODULE, HAPJsonUtility.buildJson(this.m_modules, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_modules =  HAPSerializeUtility.buildMapFromJsonObject(HAPDefinitionMiniAppModule.class.getName(), jsonObj.optJSONObject(MODULE));
		return true;
	}
	
}
