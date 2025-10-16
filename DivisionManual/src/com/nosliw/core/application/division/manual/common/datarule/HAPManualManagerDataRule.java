package com.nosliw.core.application.division.manual.common.datarule;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.entity.datarule.HAPDataRule;

@Component
public class HAPManualManagerDataRule {

	private Map<String, HAPManualTransformerDataRule> m_dataRuleTransformer = new LinkedHashMap<String, HAPManualTransformerDataRule>();
	
	public HAPManualManagerDataRule(List<HAPManualProviderDataRule> providers) {
        for(HAPManualProviderDataRule provider : providers) {
        	this.m_dataRuleTransformer.put(provider.getDataRuleType(), provider.getTransformer());
        }
	}
	
    public HAPManualDefinitionBrick transformDataRule(HAPDataRule dataRule) {
    	return this.m_dataRuleTransformer.get(dataRule.getRuleType()).transformDataRule(dataRule);
    }

}
