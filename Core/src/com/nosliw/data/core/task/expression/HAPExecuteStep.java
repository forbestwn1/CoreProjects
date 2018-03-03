package com.nosliw.data.core.task.expression;

import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.task.HAPExecutable;
import com.nosliw.data.core.task.HAPProcessTaskContext;

public abstract class HAPExecuteStep  implements HAPExecutable{

	public abstract void discover(
			Map<String, HAPVariableInfo> variablesInfo, 
			Map<String, HAPVariableInfo> localVariablesInfo, 
			Set<HAPDataTypeCriteria> exitCriterias, 
			HAPProcessTaskContext context,
			HAPDataTypeHelper dataTypeHelper);

}
