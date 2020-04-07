package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.data.core.component.HAPWithAttachment;

public interface HAPDefinitionExpressionGroup extends HAPWithAttachment{

	Map<String, HAPDefinitionExpression> getExpressions();
	
}
