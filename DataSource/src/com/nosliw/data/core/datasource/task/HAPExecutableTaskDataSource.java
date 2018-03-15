package com.nosliw.data.core.datasource.task;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.task.HAPExecutableTask;
import com.nosliw.data.core.task.HAPUpdateVariable;

public class HAPExecutableTaskDataSource implements HAPExecutableTask{

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDataTypeCriteria getOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateVariable(HAPUpdateVariable updateVar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void discoverVariable(Map<String, HAPVariableInfo> variablesInfo, HAPDataTypeCriteria expectOutputCriteria,
			HAPProcessContext context) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public List<HAPResourceId> getResourceDependency() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getReferences() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Set<String> getVariables() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setId(String id) {
		// TODO Auto-generated method stub
		
	}

}
