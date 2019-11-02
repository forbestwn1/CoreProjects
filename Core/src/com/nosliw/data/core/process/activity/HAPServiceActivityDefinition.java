package com.nosliw.data.core.process.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPServiceActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String PROVIDER = "provider";

	@HAPAttribute
	public static String PARMMAPPING = "parmMapping";

	private String m_provider;
	
	private HAPDefinitionWrapperTask m_mapping;

	public HAPServiceActivityDefinition(String type) {
		super(type);
	}

	public String getProvider() {   return this.m_provider;  }

	public HAPDefinitionWrapperTask getServiceMapping() {   return this.m_mapping;  }
	
	@Override
	public HAPContextStructure getInputContextStructure(HAPContextStructure inContextStructure) {  return inContextStructure;   }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_provider = jsonObj.optString(PROVIDER);
		this.m_mapping = HAPUtilityProcess.parseTaskDefinition(this, jsonObj);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROVIDER, this.m_provider);
	}
}
