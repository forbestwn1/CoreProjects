package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public interface HAPExpressionManager {

	void registerExpressionDefinition(HAPExpressionDefinition expressionDefinition);
	
	HAPExpressionDefinition getExpressionDefinition(String name);
	
	HAPExpression processExpression(String expressionName, Map<String, HAPDataTypeCriteria> variableCriterias);
	
}
