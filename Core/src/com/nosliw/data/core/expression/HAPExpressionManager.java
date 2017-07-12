package com.nosliw.data.core.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public interface HAPExpressionManager {

	Set<String> getExpressionDefinitionSuites();
	
	HAPExpressionDefinitionSuite getExpressionDefinitionSuite(String suiteName);
	
	void addExpressionDefinitionSuite(HAPExpressionDefinitionSuite expressionDefinitionSuite);
	
	HAPExpressionDefinition getExpressionDefinition(String suite, String name);
	
	HAPExpression processExpression(String suite, String expressionName, Map<String, HAPDataTypeCriteria> variableCriterias);
	
}
