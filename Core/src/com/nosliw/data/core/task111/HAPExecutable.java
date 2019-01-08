package com.nosliw.data.core.task111;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.updatename.HAPUpdateName;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPResourceId;

public interface HAPExecutable {

	String getType();
	
	//get output criteria
	HAPDataTypeCriteria getOutput();
	
	//update variable in executable
	void updateVariable(HAPUpdateName updateVar);

	//discover : 
	//  1. update variable info
	//  2. build matcher for variable
	//  3. find output criteria
	void discoverVariable(Map<String, HAPVariableInfo> variablesInfo, HAPDataTypeCriteria expectOutputCriteria, HAPProcessTracker processTracker);

	//resource dependency 
	List<HAPResourceId> getResourceDependency();

	Set<String> getReferences();
	
	Set<String> getVariables();
	
}
