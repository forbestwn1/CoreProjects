package com.nosliw.core.application.entity.datarule;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.core.application.HAPDomainValueStructure;

@Component
public class HAPManagerDataRule {

	private Map<String, HAPPluginParserDataRule> m_parser = new LinkedHashMap<String, HAPPluginParserDataRule>();
	
	private Map<String, HAPPluginTransformerDataRule> m_transformer = new LinkedHashMap<String, HAPPluginTransformerDataRule>();
	
	public HAPManagerDataRule() {
	}

	@Autowired
	private void setProviders(List<HAPProviderDataRule> dataRuleProviders) {
		for(HAPProviderDataRule provider : dataRuleProviders) {
			this.m_parser.put(provider.getDataRuleType(), provider.getParser());
			this.m_transformer.put(provider.getDataRuleType(), provider.getTransformer());
		}
	}
	
	public HAPDataRule parseDataRule(Object dataRuleObj) {
		JSONObject dataRuleJson = (JSONObject)dataRuleObj; 
		String ruleType = dataRuleJson.getString(HAPDataRule.RULETYPE);
		return this.m_parser.get(ruleType).parse(dataRuleObj);
	}
	
	public HAPEntityOrReference transformDataRule(HAPDataRule dataRule, HAPDomainValueStructure valueStructureDomian) {
		return this.m_transformer.get(dataRule.getRuleType()).transformDataRule(dataRule, valueStructureDomian);
	}
}
