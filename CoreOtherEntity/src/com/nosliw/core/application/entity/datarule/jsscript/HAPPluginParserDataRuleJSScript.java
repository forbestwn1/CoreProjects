package com.nosliw.core.application.entity.datarule.jsscript;

import org.json.JSONObject;

import com.nosliw.core.application.entity.datarule.HAPDataRule;
import com.nosliw.core.application.entity.datarule.HAPPluginParserDataRule;

public class HAPPluginParserDataRuleJSScript implements HAPPluginParserDataRule{

	@Override
	public HAPDataRule parse(Object obj) {
		JSONObject jsonObj = (JSONObject)obj;
		HAPDataRuleJsScript out = new HAPDataRuleJsScript();
		out.setScript(jsonObj.getString(HAPDataRuleJsScript.SCRIPT));
		return out;
	}

}
