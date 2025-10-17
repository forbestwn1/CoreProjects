package com.nosliw.core.application.division.manual.common.datarule.expression;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.common.datarule.HAPManualProviderDataRule;
import com.nosliw.core.application.division.manual.common.datarule.HAPManualTransformerDataRule;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

@Component
public class HAPManualProviderDataRuleExpression implements HAPManualProviderDataRule{

	@Autowired
	private HAPManualManagerBrick m_manualBrickMan;
	
	@Override
	public String getDataRuleType() {   return HAPConstantShared.DATARULE_TYPE_EXPRESSION;   }

	@Override
	public HAPManualTransformerDataRule getTransformer() {   return new HAPManualTransformerDataRuleExpression(this.m_manualBrickMan);   }

}
