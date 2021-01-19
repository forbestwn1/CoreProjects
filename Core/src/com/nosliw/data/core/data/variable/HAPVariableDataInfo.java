package com.nosliw.data.core.data.variable;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.matcher.HAPMatchersCombo;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

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
	public void addRule(HAPDataRule rule) {   this.m_rules.add(rule);    }
	
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
		if(ruleCriteria==null)  ruleCriteria = this.m_criteria;
		for(HAPDataRule rule : this.m_rules) {
			rule.process(ruleCriteria, runtimeEnv);
		}
	}
	
	@Override
	public boolean equals(Object obj){
		boolean out = false;
		if(obj instanceof HAPVariableDataInfo){
			HAPVariableDataInfo dataInfo = (HAPVariableDataInfo)obj;
			if(HAPBasicUtility.isEquals(this.getCriteria(), dataInfo.getCriteria()) 
				&& HAPBasicUtility.isEqualLists(this.getRules(), dataInfo.getRules()) 
				&& HAPBasicUtility.isEquals(this.getRuleMatchers(), dataInfo.getRuleMatchers())
			){
				out = true;
			}
		}
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.getCriteria()!=null)	jsonMap.put(CRITERIA, HAPSerializeManager.getInstance().toStringValue(this.getCriteria(), HAPSerializationFormat.LITERATE));
		jsonMap.put(RULE, HAPJsonUtility.buildJson(m_rules, HAPSerializationFormat.JSON));
		if(this.m_ruleMatchers!=null) jsonMap.put(RULEMATCHERS, this.m_ruleMatchers.toStringValue(HAPSerializationFormat.JSON));
		if(this.getRuleCriteria()!=null)	jsonMap.put(RULECRITERIA, HAPSerializeManager.getInstance().toStringValue(this.getRuleCriteria(), HAPSerializationFormat.LITERATE));
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
			
			JSONObject ruleMatchersObj = jsonValue.optJSONObject(RULEMATCHERS);
			if(ruleMatchersObj!=null) {
				this.m_ruleMatchers = new HAPMatchersCombo();
				this.m_ruleMatchers.buildObject(ruleMatchersObj, HAPSerializationFormat.JSON);
			}

			String ruleCriteriaStr = (String)jsonValue.opt(RULECRITERIA);
			if(ruleCriteriaStr!=null)	this.m_ruleCriteria = HAPCriteriaParser.getInstance().parseCriteria(ruleCriteriaStr);
		}
		return true;
	}
	
	public HAPVariableDataInfo cloneVariableDataInfo() {
		HAPVariableDataInfo out = new HAPVariableDataInfo();
		out.m_criteria = HAPCriteriaUtility.cloneDataTypeCriteria(this.m_criteria);
		out.m_rules.addAll(this.m_rules);
		if(this.m_ruleMatchers!=null) out.m_ruleMatchers = this.m_ruleMatchers.cloneMatchers();
		if(this.m_ruleCriteria!=null)  out.m_ruleCriteria = HAPCriteriaUtility.cloneDataTypeCriteria(this.m_ruleCriteria);
		return out;
	}
	
	@Override
	public String toString(){		return this.toStringValue(HAPSerializationFormat.JSON);	}
}
