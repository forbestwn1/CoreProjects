package com.nosliw.data.core.task.expression;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.task.HAPExecutable;
import com.nosliw.data.core.task.HAPExecuteTask;
import com.nosliw.data.core.task.HAPProcessTaskContext;
import com.nosliw.data.core.task.HAPVariableInfo;

public abstract class HAPExecuteStep  implements HAPExecutable{

	public abstract void discover(
			Map<String, HAPVariableInfo> variablesInfo, 
			Map<String, HAPVariableInfo> localVariablesInfo, 
			Set<HAPDataTypeCriteria> exitCriterias, 
			HAPProcessTaskContext context,
			HAPDataTypeHelper dataTypeHelper);

	public abstract void updateReferencedExecute(Map<String, HAPExecuteTask> references);
	
	public abstract List<HAPResourceId> discoverResources();
	
}
