package com.nosliw.core.application.common.datadefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.datarule.HAPDataRule;
import com.nosliw.core.application.common.datarule.HAPParserDataRule;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPParserCriteria;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.data.matcher.HAPMatchersCombo;

public class HAPDataDefinitionWritable extends HAPDataDefinition{

	@HAPAttribute
	public static String RULE = "rule";

	@HAPAttribute
	public static String RULEMATCHERS = "ruleMatchers";

	@HAPAttribute
	public static String RULECRITERIA = "ruleCriteria";

	//rules that apply constrain for the value
	private List<HAPDataRule> m_rules;
	
	//matchers that apply to rule
	private HAPMatchersCombo m_ruleMatchers;

	private HAPDataTypeCriteria m_ruleCriteria;

	public HAPDataDefinitionWritable() { 
		this.m_rules = new ArrayList<HAPDataRule>();
	}
	
	public HAPDataDefinitionWritable(HAPDataTypeCriteria criteria) {
		super(criteria);
		this.m_rules = new ArrayList<HAPDataRule>();
	}
	
	public List<HAPDataRule> getRules(){   return this.m_rules;   }
	public void addRule(HAPDataRule rule) {   
		if(rule!=null) {
			this.m_rules.add(rule);
		}    
	}
	
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
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPDataDefinitionWritable){
			HAPDataDefinitionWritable dataInfo = (HAPDataDefinitionWritable)obj;
			if(super.equals(dataInfo) 
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
		super.buildObject(value, format);
		if(value instanceof String) {
		}
		else if(value instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)value;
			
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
	
	protected void cloneToDataDefinitionWritable(HAPDataDefinitionWritable out) {
		this.cloneToDataDefinition(out);
		out.m_rules.addAll(this.m_rules);
		if(this.m_ruleMatchers!=null) {
			out.m_ruleMatchers = this.m_ruleMatchers.cloneMatchers();
		}
		if(this.m_ruleCriteria!=null) {
			out.m_ruleCriteria = HAPUtilityCriteria.cloneDataTypeCriteria(this.m_ruleCriteria);
		}
	}
	
	public HAPDataDefinitionWritable cloneDataDefinitionWritable() {
		HAPDataDefinitionWritable out = new HAPDataDefinitionWritable();
		this.cloneToDataDefinitionWritable(out);
		return out;
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}
}
