package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

public interface HAPExpressionDefinitionProcessor {

	/**
	 * Process expression definition
	 * 		parse it to operand
	 * 		solve constant
	 * 		solve reference
	 * 		discovery 
	 * @param id  the id assigned to expression
	 * @param expDef   expression definition need to process
	 * @param contextExpressionDefinitions   other expressions that may need during solving reference
	 * @param variableCriterias   variable criterias that need to respect during discovery
	 * @return
	 */
	HAPExpression processExpressionDefinition(
			String id, 
			HAPExpressionDefinition expDef, 
			Map<String, HAPExpressionDefinition> contextExpressionDefinitions, 
			Map<String, HAPData> contextConstants,
			Map<String, HAPDataTypeCriteria> variableCriterias,
			HAPProcessExpressionDefinitionContext context
	);

	/**
	 * parse expression to build operand
	 * @param expression
	 * @return
	 */
	HAPOperand parseExpression(String expression);

	
}
