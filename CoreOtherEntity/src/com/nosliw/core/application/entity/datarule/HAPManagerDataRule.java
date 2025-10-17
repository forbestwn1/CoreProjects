package com.nosliw.core.application.entity.datarule;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class HAPManagerDataRule {

	private Map<String, HAPPluginParserDataRule> m_parser = new LinkedHashMap<String, HAPPluginParserDataRule>();
	
	public HAPManagerDataRule(List<HAPProviderDataRule> dataRuleProviders) {
		this.m_parser = new LinkedHashMap<String, HAPPluginParserDataRule>();
		for(HAPProviderDataRule provider : dataRuleProviders) {
			this.m_parser.put(provider.getDataRuleType(), provider.getParser());
		}
	}
	
	public HAPDataRule parseDataRule(Object dataRuleObj) {
		JSONObject dataRuleJson = (JSONObject)dataRuleObj; 
		String ruleType = dataRuleJson.getString(HAPDataRule.RULETYPE);
		return this.m_parser.get(ruleType).parse(dataRuleObj);
	}
}
