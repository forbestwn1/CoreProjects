package com.nosliw.core.application.entity.datarule.mandatory;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPPluginParserDataRule;
import com.nosliw.core.application.entity.datarule.HAPPluginTransformerDataRule;
import com.nosliw.core.application.entity.datarule.HAPProviderDataRule;

@Component
public class HAPProviderDataRuleMandatory implements HAPProviderDataRule{

	@Override
	public String getDataRuleType() {  return HAPConstantShared.DATARULE_TYPE_MANDATORY;  }

	@Override
	public HAPPluginParserDataRule getParser() {
		return new HAPPluginParserDataRuleMandatory();
	}

	@Override
	public HAPPluginTransformerDataRule getTransformer() {
		// TODO Auto-generated method stub
		return null;
	}

}
