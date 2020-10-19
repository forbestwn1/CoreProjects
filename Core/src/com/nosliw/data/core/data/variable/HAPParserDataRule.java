package com.nosliw.data.core.data.variable;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;

public class HAPParserDataRule {

	public static HAPDataRule parseRule(Object ruleObj) {
		HAPDataRule out = null;
		JSONObject ruleJsonObj = (JSONObject)ruleObj;
		String ruleType = ruleJsonObj.getString(HAPDataRule.RULETYPE);
		if(ruleType.equals(HAPConstant.DATARULE_TYPE_ENUM)) {
			String enumCode = (String)ruleJsonObj.opt(HAPDataRuleEnumCode.ENUMCODE);
			if(enumCode!=null) 	out = new HAPDataRuleEnumCode();
			else out = new HAPDataRuleEnumData();
			out.buildObject(ruleObj, HAPSerializationFormat.JSON);
		}
		else if(ruleType.equals(HAPConstant.DATARULE_TYPE_EXPRESSION)) {
			
		}
		else if(ruleType.equals(HAPConstant.DATARULE_TYPE_MANDATORY)) {
			
		}
		
		return out;
	}
	
}
