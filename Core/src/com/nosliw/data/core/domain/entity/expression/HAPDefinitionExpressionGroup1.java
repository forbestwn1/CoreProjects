package com.nosliw.data.core.domain.entity.expression;

import com.nosliw.data.core.common.HAPWithConstantDefinition;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public interface HAPDefinitionExpressionGroup1 extends 
			HAPWithEntityElement<HAPDefinitionExpression>, 
			HAPDefinitionEntityInDomainComplex, 
			HAPWithConstantDefinition{

	HAPDefinitionExpressionGroup1 cloneExpressionGroupDefinition();
	
}
