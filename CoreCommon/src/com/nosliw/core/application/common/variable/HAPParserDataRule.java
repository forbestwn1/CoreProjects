package com.nosliw.core.application.common.variable;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPParserDataRule {

	public static HAPDataRule parseRule(Object ruleObj) {
		HAPDataRule out = null;
		JSONObject ruleJsonObj = (JSONObject)ruleObj;
		String ruleType = ruleJsonObj.getString(HAPDataRule.RULETYPE);
		if(ruleType.equals(HAPConstantShared.DATARULE_TYPE_ENUM)) {
			String enumCode = (String)ruleJsonObj.opt(HAPDataRuleEnumCode.ENUMCODE);
			if(enumCode!=null) 	out = new HAPDataRuleEnumCode();
			else out = new HAPDataRuleEnumData();
			out.buildObject(ruleObj, HAPSerializationFormat.JSON);
		}
		else if(ruleType.equals(HAPConstantShared.DATARULE_TYPE_EXPRESSION)) {
			out = new HAPDataRuleExpression();
			out.buildObject(ruleObj, HAPSerializationFormat.JSON);
		}
		else if(ruleType.equals(HAPConstantShared.DATARULE_TYPE_JSSCRIPT)) {
			out = new HAPDataRuleJsScript();
			out.buildObject(ruleObj, HAPSerializationFormat.JSON);
		}
		else if(ruleType.equals(HAPConstantShared.DATARULE_TYPE_MANDATORY)) {
			out = new HAPDataRuleMandatory();
			out.buildObject(ruleObj, HAPSerializationFormat.JSON);
		}
		
		return out;
	}
	
}
