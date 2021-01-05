package com.nosliw.data.core.data.variable;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.data.criteria.HAPCriteriaParser;
import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPVariableDataInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String CRITERIA = "criteria";

	@HAPAttribute
	public static String RULE = "rule";

	//data type
	private HAPDataTypeCriteria m_criteria;

	//rules that apply constrain for the value
	private Set<HAPDataRule> m_rules;
	
	public HAPVariableDataInfo() {
		this.m_rules = new HashSet<HAPDataRule>();
	}
	
	public HAPVariableDataInfo(HAPDataTypeCriteria criteria) {
		this();
		this.m_criteria = criteria;
	}
	
	public HAPDataTypeCriteria getCriteria() {   return this.m_criteria; }
	public void setCriteria(HAPDataTypeCriteria criteria) {    this.m_criteria = criteria;     }
	
	public Set<HAPDataRule> getRules(){   return this.m_rules;   }
	public void addRule(HAPDataRule rule) {   this.m_rules.add(rule);    }
	
	public void process(HAPRuntimeEnvironment runtimeEnv) {
		for(HAPDataRule rule : this.m_rules) {
			rule.process(this.m_criteria, runtimeEnv);
		}
	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPVariableDataInfo){
			HAPVariableDataInfo dataInfo = (HAPVariableDataInfo)obj;
			if(HAPBasicUtility.isEquals(this.getCriteria(), dataInfo.getCriteria()) && HAPBasicUtility.isEqualSet(this.getRules(), dataInfo.getRules())){
				out = true;
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.getCriteria()!=null){
			jsonMap.put(CRITERIA, HAPSerializeManager.getInstance().toStringValue(this.getCriteria(), HAPSerializationFormat.LITERATE));
		}
		jsonMap.put(RULE, HAPJsonUtility.buildJson(m_rules, HAPSerializationFormat.JSON));
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_criteria = HAPCriteriaParser.getInstance().parseCriteria((String)value);
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.m_criteria = HAPCriteriaParser.getInstance().parseCriteria((String)jsonValue.opt(CRITERIA));
			
			JSONArray ruleJsonArray = jsonValue.optJSONArray(RULE);
			if(ruleJsonArray!=null) {
				for(int i=0; i<ruleJsonArray.length(); i++) {
					this.m_rules.add(HAPParserDataRule.parseRule(ruleJsonArray.get(i)));
				}
			}
		}
		return true;
	}
	
	public HAPVariableDataInfo cloneVariableDataInfo() {
		HAPVariableDataInfo out = new HAPVariableDataInfo();
		out.m_criteria = HAPCriteriaUtility.cloneDataTypeCriteria(this.m_criteria);
		out.m_rules.addAll(this.m_rules);
		return out;
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}
}
