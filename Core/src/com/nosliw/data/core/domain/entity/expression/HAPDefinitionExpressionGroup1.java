package com.nosliw.data.core.domain.entity.expression;

import com.nosliw.data.core.common.HAPWithConstantDefinition;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;

public interface HAPDefinitionExpressionGroup1 extends 
			HAPWithEntityElement<HAPDefinitionExpression>, 
			HAPDefinitionEntityComplex, 
			HAPWithConstantDefinition{

	HAPDefinitionExpressionGroup1 cloneExpressionGroupDefinition();
	
}
