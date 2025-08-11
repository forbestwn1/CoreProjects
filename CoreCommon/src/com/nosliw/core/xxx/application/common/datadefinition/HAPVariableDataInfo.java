package com.nosliw.core.xxx.application.common.datadefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.datadefinition.HAPDataRule;
import com.nosliw.core.application.common.datadefinition.HAPParserDataRule;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPParserCriteria;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.data.matcher.HAPMatchersCombo;
import com.nosliw.core.runtimeenv.HAPRuntimeEnvironment;

@HAPEntityWithAttribute
public class HAPVariableDataInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String CRITERIA = "criteria";

	@HAPAttribute
	public static String RULE = "rule";

	@HAPAttribute
	public static String RULEMATCHERS = "ruleMatchers";

	@HAPAttribute
	public static String RULECRITERIA = "ruleCriteria";

	//data type
	private HAPDataTypeCriteria m_criteria;

	//rules that apply constrain for the value
	private List<HAPDataRule> m_rules;
	
	//matchers that apply to rule
	private HAPMatchersCombo m_ruleMatchers;

	
	private HAPDataTypeCriteria m_ruleCriteria;
	
	public HAPVariableDataInfo() {
		this.m_rules = new ArrayList<HAPDataRule>();
	}
	
	public HAPVariableDataInfo(HAPDataTypeCriteria criteria) {
		this();
		this.m_criteria = criteria;
	}
	
	public HAPDataTypeCriteria getCriteria() {   return this.m_criteria; }
	public void setCriteria(HAPDataTypeCriteria criteria) {    this.m_criteria = criteria;     }
	
	public List<HAPDataRule> getRules(){   return this.m_rules;   }
	public void addRule(HAPDataRule rule) {   if(rule!=null) {
		this.m_rules.add(rule);
	}    }
	
	public HAPMatchersCombo getRuleMatchers() {    return this.m_ruleMatchers;    }
	public void setRuleMatchers(HAPMatchers matchers, HAPDataTypeCriteria ruleCriteria) {
		if(matchers!=null) {
			this.m_ruleMatchers = new HAPMatchersCombo(matchers);
			this.m_ruleCriteria = ruleCriteria;
		}
		else {
			this.m_ruleMatchers = null;
			this.m_ruleCriteria = null;
		}
	}
	public HAPDataTypeCriteria getRuleCriteria() {   return this.m_ruleCriteria;   }
	
	public void process(HAPRuntimeEnvironment runtimeEnv) {
		HAPDataTypeCriteria ruleCriteria = this.m_ruleCriteria;
		if(ruleCriteria==null) {
			ruleCriteria = this.m_criteria;
		}
		for(HAPDataRule rule : this.m_rules) {
			rule.process(ruleCriteria, runtimeEnv);
		}
	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPVariableDataInfo){
			HAPVariableDataInfo dataInfo = (HAPVariableDataInfo)obj;
			if(HAPUtilityBasic.isEquals(this.getCriteria(), dataInfo.getCriteria()) 
				&& HAPUtilityBasic.isEqualLists(this.getRules(), dataInfo.getRules()) 
				&& HAPUtilityBasic.isEquals(this.getRuleMatchers(), dataInfo.getRuleMatchers())
			){
				out = true;
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.getCriteria()!=null) {
			jsonMap.put(CRITERIA, HAPManagerSerialize.getInstance().toStringValue(this.getCriteria(), HAPSerializationFormat.LITERATE));
		}
		jsonMap.put(RULE, HAPUtilityJson.buildJson(m_rules, HAPSerializationFormat.JSON));
		if(this.m_ruleMatchers!=null) {
			jsonMap.put(RULEMATCHERS, this.m_ruleMatchers.toStringValue(HAPSerializationFormat.JSON));
		}
		if(this.getRuleCriteria()!=null) {
			jsonMap.put(RULECRITERIA, HAPManagerSerialize.getInstance().toStringValue(this.getRuleCriteria(), HAPSerializationFormat.LITERATE));
		}
	}
	
	@Override
	public boolean buildObject(Object value, HAPSerializationFormat format) {
		if(value instanceof String) {
			this.m_criteria = HAPParserCriteria.getInstance().parseCriteria((String)value);
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			this.m_criteria = HAPParserCriteria.getInstance().parseCriteria((String)jsonValue.opt(CRITERIA));
			
			JSONArray ruleJsonArray = jsonValue.optJSONArray(RULE);
			if(ruleJsonArray!=null) {
				for(int i=0; i<ruleJsonArray.length(); i++) {
					this.addRule(HAPParserDataRule.parseRule(ruleJsonArray.get(i)));
				}
			}
			
			JSONObject ruleMatchersObj = jsonValue.optJSONObject(RULEMATCHERS);
			if(ruleMatchersObj!=null) {
				this.m_ruleMatchers = new HAPMatchersCombo();
				this.m_ruleMatchers.buildObject(ruleMatchersObj, HAPSerializationFormat.JSON);
			}

			String ruleCriteriaStr = (String)jsonValue.opt(RULECRITERIA);
			if(ruleCriteriaStr!=null) {
				this.m_ruleCriteria = HAPParserCriteria.getInstance().parseCriteria(ruleCriteriaStr);
			}
		}
		return true;
	}
	
	public HAPVariableDataInfo cloneVariableDataInfo() {
		HAPVariableDataInfo out = new HAPVariableDataInfo();
		out.m_criteria = HAPUtilityCriteria.cloneDataTypeCriteria(this.m_criteria);
		out.m_rules.addAll(this.m_rules);
		if(this.m_ruleMatchers!=null) {
			out.m_ruleMatchers = this.m_ruleMatchers.cloneMatchers();
		}
		if(this.m_ruleCriteria!=null) {
			out.m_ruleCriteria = HAPUtilityCriteria.cloneDataTypeCriteria(this.m_ruleCriteria);
		}
		return out;
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}
}
