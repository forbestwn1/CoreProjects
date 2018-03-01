package com.nosliw.data.core.task;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPProcessExpressionDefinitionContext;

public interface HAPProcessable {

	HAPExecutable process(
			String id, 
			Map<String, HAPDefinitionTask> contextTaskDefinitions,
			Map<String, HAPData> contextConstants,
			Map<String, HAPDataTypeCriteria> variableCriterias, 
			HAPProcessExpressionDefinitionContext context);
	
}
