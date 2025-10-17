package com.nosliw.core.application.entity.datarule;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;

@HAPEntityWithAttribute
public abstract class HAPDataRule extends HAPSerializableImp{

	@HAPAttribute
	public static String RULETYPE = "ruleType";

	private String m_ruleType;
	
	private HAPDataRuleImplementation m_implementation;
	
	private HAPDataTypeCriteria m_dataCriteria;
	
	public HAPDataRule(String type) {
		this.m_ruleType = type;
	}
	
	public String getRuleType() {   return this.m_ruleType;    }

	public HAPDataTypeCriteria getDataCriteria() {		return this.m_dataCriteria; 	}
	public void setDataCriteria(HAPDataTypeCriteria dataCriteria) {    this.m_dataCriteria = dataCriteria;       }

	public HAPDataRuleImplementation getImplementation() {	return this.m_implementation;	}
	public void setImplementation(HAPDataRuleImplementation imp) {  this.m_implementation = imp;     }

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
