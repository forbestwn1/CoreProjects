package com.nosliw.core.application.entity.datarule;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.enum1.HAPDataRuleEnumCode;
import com.nosliw.core.application.entity.datarule.enum1.HAPDataRuleEnumData;
import com.nosliw.core.application.entity.datarule.expression.HAPDataRuleExpression;
import com.nosliw.core.application.entity.datarule.jsscript.HAPDataRuleJsScript;
import com.nosliw.core.application.entity.datarule.mandatory.HAPDataRuleMandatory;

public class HAPParserDataRule1 {

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
