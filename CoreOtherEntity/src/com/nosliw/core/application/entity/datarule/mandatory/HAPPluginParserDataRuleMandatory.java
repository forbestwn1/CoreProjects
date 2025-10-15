package com.nosliw.core.application.entity.datarule.mandatory;

import org.json.JSONObject;

import com.nosliw.core.application.entity.datarule.HAPDataRule;
import com.nosliw.core.application.entity.datarule.HAPPluginParserDataRule;

public class HAPPluginParserDataRuleMandatory implements HAPPluginParserDataRule{

	@Override
	public HAPDataRule parse(Object obj) {
		JSONObject jsonObj = (JSONObject)obj;
		HAPDataRuleMandatory out = new HAPDataRuleMandatory();
		return out;
	}

}
