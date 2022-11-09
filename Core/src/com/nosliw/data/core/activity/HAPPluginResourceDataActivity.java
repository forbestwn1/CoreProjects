package com.nosliw.data.core.activity;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValueImp;

public class HAPPluginResourceDataActivity extends HAPResourceDataJSValueImp{

	private HAPPluginActivity m_activityPlugin;
	
	private String m_env;
	
	public HAPPluginResourceDataActivity(HAPPluginActivity activityPlugin, String env){
		this.m_activityPlugin = activityPlugin;
		this.m_env = env;
	}
	
	@Override
	public String getValue() {
		Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		outJsonMap.put(HAPPluginActivity.SCRIPT, this.m_activityPlugin.getScript(m_env).getScript());
		typeJsonMap.put(HAPPluginActivity.SCRIPT, HAPJsonTypeScript.class);
		this.buildFullJsonMap(outJsonMap, typeJsonMap);
		return HAPUtilityJson.buildMapJson(outJsonMap, typeJsonMap);
	}

}
