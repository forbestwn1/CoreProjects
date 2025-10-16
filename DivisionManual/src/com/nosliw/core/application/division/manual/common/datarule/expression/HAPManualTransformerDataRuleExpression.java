package com.nosliw.core.application.division.manual.common.datarule.expression;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.common.datarule.HAPManualTransformerDataRule;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.entity.datarule.HAPDataRule;
import com.nosliw.core.application.entity.datarule.expression.HAPDataRuleExpression;

public class HAPManualTransformerDataRuleExpression implements HAPManualTransformerDataRule{

	private HAPManualManagerBrick m_manualBrickMan;
	
	@Override
	public HAPManualDefinitionBrick transformDataRule(HAPDataRule dataRule) {
		HAPDataRuleExpression expressionDataRule = (HAPDataRuleExpression)dataRule;
		
		this.m_manualBrickMan.newBrickDefinition(HAPEnumBrickType.DATAEXPRESSIONSTANDALONE_100);
		
		return null;
	}

}
