package com.nosliw.data.core.process;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPResourceId;

public interface HAPExecutable {

	String getType();
	
	//update variable in executable
//	void updateVariable(HAPUpdateVariable updateVar);

	//discover : 
	//  1. update variable info
	//  2. build matcher for variable
	//  3. find output criteria
//	void discoverVariable(Map<String, HAPVariableInfo> variablesInfo, HAPDataTypeCriteria expectOutputCriteria, HAPProcessContext context);

	//resource dependency 
//	List<HAPResourceId> getResourceDependency();

//	Set<String> getReferences();
	
//	Set<String> getVariables();
	
}
