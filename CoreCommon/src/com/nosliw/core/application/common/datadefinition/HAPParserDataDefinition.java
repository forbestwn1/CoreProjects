package com.nosliw.core.application.common.datadefinition;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.core.application.entity.datarule.HAPDataRule;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;
import com.nosliw.core.data.HAPUtilityData;
import com.nosliw.core.data.criteria.HAPParserCriteria;

public class HAPParserDataDefinition {

	public static HAPDataDefinitionWritableWithInit parseDataDefinitionWritableWithInit(Object obj, HAPManagerDataRule dataRuleManager) {
		HAPDataDefinitionWritableWithInit out = new HAPDataDefinitionWritableWithInit();
		
		parseToDataDefinitionWritable(obj, out, dataRuleManager);

		if(obj instanceof String) {
		}
		else if(obj instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)obj;
			
			Object initDataObj = jsonValue.opt(HAPDataDefinitionWritableWithInit.INITDATA);
			if(initDataObj!=null) {
				out.setInitData(HAPUtilityData.buildDataWrapperFromObject(initDataObj));
			}
		}
		return out;
	}
	
	public static HAPDataDefinitionWritable parseDataDefinitionWritable(Object obj, HAPManagerDataRule dataRuleManager) {
		HAPDataDefinitionWritable out = new HAPDataDefinitionWritable();
		parseToDataDefinitionWritable(obj, out, dataRuleManager);
		return out;
	}

	private static void parseToDataDefinitionWritable(Object obj, HAPDataDefinitionWritable dataDefWritable, HAPManagerDataRule dataRuleManager) {
		parseToDataDefinition(obj, dataDefWritable);

		if(obj instanceof String) {
		}
		else if(obj instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)obj;
			
			JSONArray ruleJsonArray = jsonValue.optJSONArray(HAPDataDefinitionWritable.RULE);
			if(ruleJsonArray!=null) {
				for(int i=0; i<ruleJsonArray.length(); i++) {
					JSONObject dataRuleDefJson = ruleJsonArray.getJSONObject(i);
					if(HAPUtilityEntityInfo.isEnabled(dataRuleDefJson)) {
						HAPDefinitionDataRule dataRuleDef = new HAPDefinitionDataRule();
						
						dataRuleDef.buildEntityInfoByJson(dataRuleDefJson);
						
						String rulePath = (String)dataRuleDefJson.opt(HAPDefinitionDataRule.PATH);
						if(rulePath!=null) {
							dataRuleDef.setPath(rulePath);
						}
						
						JSONObject dataRuleJson = dataRuleDefJson.optJSONObject(HAPDefinitionDataRule.DATARULE);
						if(dataRuleJson==null) {
							dataRuleJson = dataRuleDefJson;
						}
						HAPDataRule dataRule = dataRuleManager.parseDataRule(dataRuleJson);
						dataRuleDef.setDataRule(dataRule);
						
						dataDefWritable.addRule(dataRuleDef);
					}
				}
			}
			
//			JSONObject ruleMatchersObj = jsonValue.optJSONObject(RULEMATCHERS);
//			if(ruleMatchersObj!=null) {
//				this.m_ruleMatchers = new HAPMatchersCombo();
//				this.m_ruleMatchers.buildObject(ruleMatchersObj, HAPSerializationFormat.JSON);
//			}
//
//			String ruleCriteriaStr = (String)jsonValue.opt(RULECRITERIA);
//			if(ruleCriteriaStr!=null) {
//				this.m_ruleCriteria = HAPParserCriteria.getInstance().parseCriteria(ruleCriteriaStr);
//			}
			
		}
	}

	public static HAPDataDefinitionReadonly parseDataDefinitionReadonly(Object obj) {
		HAPDataDefinitionReadonly out = new HAPDataDefinitionReadonly();
		parseToDataDefinition(obj, out);
		return out;
	}
	
	private static void parseToDataDefinition(Object obj, HAPDataDefinition dataDefinition) {
		if(obj instanceof String) {
			dataDefinition.setCriteria(HAPParserCriteria.getInstance().parseCriteria((String)obj));
		}
		else if(obj instanceof JSONObject){
			JSONObject jsonValue = (JSONObject)obj;
			dataDefinition.setCriteria(HAPParserCriteria.getInstance().parseCriteria((String)jsonValue.opt(HAPDataDefinition.CRITERIA)));
		}
	}
	
}
