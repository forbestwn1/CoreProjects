package com.nosliw.core.application.entity.datarule.jsscript;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPPluginParserDataRule;
import com.nosliw.core.application.entity.datarule.HAPProviderDataRule;

@Component
public class HAPProviderDataRuleJSScript implements HAPProviderDataRule{

	@Override
	public String getDataRuleType() {  return HAPConstantShared.DATARULE_TYPE_JSSCRIPT;  }

	@Override
	public HAPPluginParserDataRule getParser() {
		return new HAPPluginParserDataRuleJSScript();
	}

}
