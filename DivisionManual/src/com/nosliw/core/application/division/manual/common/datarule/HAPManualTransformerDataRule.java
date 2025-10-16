package com.nosliw.core.application.division.manual.common.datarule;

import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.entity.datarule.HAPDataRule;

public interface HAPManualTransformerDataRule {

	HAPManualDefinitionBrick transformDataRule(HAPDataRule dataRule);
	
}
