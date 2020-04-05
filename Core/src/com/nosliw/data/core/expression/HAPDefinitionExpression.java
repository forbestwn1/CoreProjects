package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.data.core.component.HAPWithAttachment;

public interface HAPDefinitionExpression extends HAPWithAttachment{

	Map<String, HAPDefinitionExpressionSuiteElementEntityExpression> getExpressionElements();
	
	
}
