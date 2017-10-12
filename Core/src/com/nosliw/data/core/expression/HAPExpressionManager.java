package com.nosliw.data.core.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public interface HAPExpressionManager {

	Set<String> getExpressionDefinitionSuites();
	
	HAPExpressionDefinitionSuite getExpressionDefinitionSuite(String suiteName);
	
	void addExpressionDefinitionSuite(HAPExpressionDefinitionSuite expressionDefinitionSuite);
	
	HAPExpressionDefinition getExpressionDefinition(String suite, String name);
	
	HAPExpression processExpression(String id, String suite, String expressionName, Map<String, HAPDataTypeCriteria> variableCriterias);

	HAPExpression processExpression(String id, HAPExpressionDefinition expressionDefinition, Map<String, HAPData> contextConstants, Map<String, HAPDataTypeCriteria> variableCriterias, Map<String, String> context);

	//create a simple expression definition
	HAPExpressionDefinition newExpressionDefinition(String expression, String name, Map<String, HAPData> constants, Map<String, HAPDataTypeCriteria> variableCriterias); 

	HAPExpressionDefinitionSuite newExpressionDefinitionSuite(String name);
}
