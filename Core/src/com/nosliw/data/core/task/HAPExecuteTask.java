package com.nosliw.data.core.task;

import java.util.List;
import java.util.Map;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.runtime.HAPResourceId;

public interface HAPExecuteTask extends HAPExecutable{

	HAPMatchers discoverVariable(Map<String, HAPVariableInfo> variablesInfo, HAPDataTypeCriteria expectOutputCriteria, HAPProcessTaskContext context, HAPDataTypeHelper dataTypeHelper);

	List<HAPResourceId> discoverResources();
	
	Map<String, HAPMatchers> getVariableMatchers();
}
