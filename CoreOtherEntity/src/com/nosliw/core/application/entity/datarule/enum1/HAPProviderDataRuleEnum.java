package com.nosliw.core.application.entity.datarule.enum1;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.entity.datarule.HAPPluginParserDataRule;
import com.nosliw.core.application.entity.datarule.HAPPluginTransformerDataRule;
import com.nosliw.core.application.entity.datarule.HAPProviderDataRule;

@Component
public class HAPProviderDataRuleEnum implements HAPProviderDataRule{

	@Override
	public String getDataRuleType() {  return HAPConstantShared.DATARULE_TYPE_ENUM;  }

	@Override
	public HAPPluginParserDataRule getParser() {
		return new HAPPluginParserDataRuleEnum();
	}

	@Override
	public HAPPluginTransformerDataRule getTransformer() {
		// TODO Auto-generated method stub
		return null;
	}

}
