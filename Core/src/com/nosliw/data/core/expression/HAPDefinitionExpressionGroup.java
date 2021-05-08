package com.nosliw.data.core.expression;

import com.nosliw.common.info.HAPEntityInfoWritable;
import com.nosliw.data.core.common.HAPWithConstantDefinition;
import com.nosliw.data.core.common.HAPWithValueStructure;
import com.nosliw.data.core.common.HAPWithEntityElement;

public interface HAPDefinitionExpressionGroup extends 
			HAPWithEntityElement<HAPDefinitionExpression>, 
			HAPEntityInfoWritable, 
			HAPWithValueStructure, 
			HAPWithConstantDefinition{

	HAPDefinitionExpressionGroup cloneExpressionGroupDefinition();
	
}
