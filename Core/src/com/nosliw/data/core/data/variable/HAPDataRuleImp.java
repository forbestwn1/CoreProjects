package com.nosliw.data.core.data.variable;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPDataRuleImp extends HAPEntityInfoImp implements HAPDataRule{

	private String m_ruleType;
	
	private String m_path;
	
	public HAPDataRuleImp() {
	}
	
	public HAPDataRuleImp(String type) {
		this.m_ruleType = type;
	}
	
	@Override
	public String getRuleType() {   return this.m_ruleType;    }
	
	@Override
	public String getPath() {    return this.m_path;     }

	@Override
	public void process(HAPDataTypeCriteria criteria, HAPRuntimeEnvironment runtimeEnv) {}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(RULETYPE, this.getRuleType());
		jsonMap.put(PATH, this.getPath());
	}
	
	@Override
	public boolean buildObjectByJson(Object value) {
		JSONObject valueObj = (JSONObject)value;
		super.buildObjectByJson(valueObj);
		this.m_ruleType = valueObj.getString(RULETYPE);
		this.m_path = (String)valueObj.opt(PATH);
		return true;
	}

}
