package com.nosliw.data.core.process.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.process.HAPDefinitionActivityTask;

public class HAPProcessActivityDefinition extends HAPDefinitionActivityTask{

	@HAPAttribute
	public static String PROCESS = "process";
	
	private String m_process;
	
	public HAPProcessActivityDefinition(String type) {
		super(type);
	}
	
	public String getProcess(){  return this.m_process;    }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_process = jsonObj.optString(PROCESS);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROCESS, this.m_process);
	}
}
