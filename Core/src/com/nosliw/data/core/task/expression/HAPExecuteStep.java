package com.nosliw.data.core.task.expression;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.task.HAPExecutable;
import com.nosliw.data.core.task.HAPExecutableTask;

public abstract class HAPExecuteStep  implements HAPExecutable{

	public abstract void discover(
			Map<String, HAPVariableInfo> variablesInfo, 
			Map<String, HAPVariableInfo> localVariablesInfo, 
			Set<HAPVariableInfo> exitCriterias, 
			HAPProcessContext context,
			HAPDataTypeHelper dataTypeHelper);

	public abstract void updateReferencedExecute(Map<String, HAPExecutableTask> references);
	
	public abstract List<HAPResourceId> discoverResources();
	
}
