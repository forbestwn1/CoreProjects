package com.nosliw.core.application.configure;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.resource.HAPResourceDataImp;

@HAPEntityWithAttribute
public class HAPResourceDataConfigure extends HAPResourceDataImp{

	@HAPAttribute
	public static final String CONFIGURE = "configure";

	private JSONObject m_configureJson;

	public HAPResourceDataConfigure(JSONObject configureJson) {	
		this.m_configureJson = configureJson;
	}

	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(CONFIGURE, this.m_configureJson.toString());
	}
}
