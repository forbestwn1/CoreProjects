package com.nosliw.data.core.process.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPScript;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.process.plugin.HAPPluginActivity;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValueImp;

public class HAPResourceDataActivityPlugin extends HAPResourceDataJSValueImp{

	private HAPPluginActivity m_activityPlugin;
	
	private String m_env;
	
	public HAPResourceDataActivityPlugin(HAPPluginActivity activityPlugin, String env){
		this.m_activityPlugin = activityPlugin;
		this.m_env = env;
	}
	
	@Override
	public String getValue() {
		Map<String, String> outJsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		outJsonMap.put(HAPPluginActivity.SCRIPT, this.m_activityPlugin.getScript(m_env).getScript());
		typeJsonMap.put(HAPPluginActivity.SCRIPT, HAPScript.class);
		this.buildFullJsonMap(outJsonMap, typeJsonMap);
		return HAPJsonUtility.buildMapJson(outJsonMap, typeJsonMap);
	}

}
