package com.nosliw.data.core.process;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPDefinitionActivityTask extends HAPDefinitionActivityNormal{

	private HAPDefinitionWrapperTask m_taskMapping;

	public HAPDefinitionActivityTask(String type) {
		super(type);
	}

	public HAPDefinitionWrapperTask getTaskMapping() {   return this.m_taskMapping;  }
	
	@Override
	public HAPContextStructure getInputContextStructure(HAPContextStructure parentContextStructure) {  return parentContextStructure;   }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_taskMapping = HAPUtilityProcess.parseTaskDefinition(this, jsonObj);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
