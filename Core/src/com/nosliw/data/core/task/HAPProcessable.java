package com.nosliw.data.core.task;

import java.util.Map;

import com.nosliw.data.core.HAPData;

import com.nosliw.data.core.expression.HAPProcessExpressionDefinitionContext;
import com.nosliw.data.core.expression.HAPVariableInfo;

public interface HAPProcessable {

	HAPExecutable process(
			String id, 
			Map<String, HAPDefinitionTask> contextTaskDefinitions,
			Map<String, HAPData> contextConstants,
			Map<String, HAPVariableInfo> variableCriterias, 
			HAPProcessExpressionDefinitionContext context);
	
}
