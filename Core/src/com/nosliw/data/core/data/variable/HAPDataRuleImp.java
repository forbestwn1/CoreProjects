package com.nosliw.data.core.data.variable;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;

public abstract class HAPDataRuleImp extends HAPSerializableImp implements HAPDataRule{

	private String m_ruleType;
	
	public HAPDataRuleImp(String type) {
		this.m_ruleType = type;
	}
	
	@Override
	public String getRuleType() {   return this.m_ruleType;    }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RULETYPE, this.getRuleType());
	}
	
	@Override
	public boolean buildObjectByJson(Object value) {
		JSONObject valueObj = (JSONObject)value;
		super.buildObjectByJson(valueObj);
		this.m_ruleType = valueObj.getString(RULETYPE);
		return true;
	}

}