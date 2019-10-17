package com.nosliw.data.core.process.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPProcessActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String PROCESS = "process";
	
	private String m_process;
	
	private HAPDefinitionWrapperTask m_mapping;
	
	public HAPProcessActivityDefinition(String type) {
		super(type);
	}
	
	public String getProcess(){  return this.m_process;    }
	public HAPDefinitionWrapperTask getMapping(){   return this.m_mapping;   }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_process = jsonObj.optString(PROCESS);
		this.m_mapping = HAPUtilityProcess.parseTaskDefinition(this, jsonObj);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROCESS, this.m_process);
	}
}
