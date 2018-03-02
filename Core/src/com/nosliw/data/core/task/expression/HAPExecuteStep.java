package com.nosliw.data.core.task.expression;

import java.util.Map;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPProcessExpressionDefinitionContext;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.task.HAPExecutable;
import com.nosliw.data.core.task.HAPProcessTaskContext;

public abstract class HAPExecuteStep  implements HAPExecutable{

	public abstract HAPMatchers discover(
			Map<String, HAPVariableInfo> variablesInfo, 
			Map<String, HAPVariableInfo> localVariablesInfo, 
			HAPDataTypeCriteria expectOutputCriteria, 
			HAPProcessTaskContext context,
			HAPDataTypeHelper dataTypeHelper);
}
