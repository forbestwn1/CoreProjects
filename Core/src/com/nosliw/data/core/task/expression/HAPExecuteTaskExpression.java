package com.nosliw.data.core.task.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaOr;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.task.HAPExecutable;
import com.nosliw.data.core.task.HAPProcessTaskContext;
import com.nosliw.data.core.task.HAPUpdateVariable;

public class HAPExecuteTaskExpression implements HAPExecutable{

	//unique in system
	private String m_id;
	
	private String m_domain;  //?? intermediate value
	
	private HAPDefinitionTaskExpression m_taskDefinition;
	
	//referenced expression
	private Map<String, HAPExecutable> m_executeReferences;
	
	private List<HAPExecuteStep> m_steps;

	// external variables info for expression
	// it is what this expression required for input
	private Map<String, HAPDataTypeCriteria> m_variables;
	
	private HAPDataTypeCriteria m_output;
	
	// store all the matchers from variables info to internal variables info in expression
	// it convert variable from caller to variable in expression
	private Map<String, HAPMatchers> m_varsMatchers;
	
	public HAPExecuteTaskExpression(HAPDefinitionTaskExpression taskDef, String domain) {
		this.m_taskDefinition = taskDef;
		this.m_domain = domain;
	}
	
	public void setId(String id) {  this.m_id = id;   }
	
	public void addReferencedExecute(String refName, HAPExecutable execute) {	this.m_executeReferences.put(refName, execute);	}
	
	public void addStep(HAPExecuteStep step) {  this.m_steps.add(step);   }
	
	public String getDomain() {  return this.m_domain;  }
	
	@Override
	public HAPDataTypeCriteria getOutput() {  return this.m_output;  }

	@Override
	public void updateVariable(HAPUpdateVariable updateVar) {
		
		for(HAPExecuteStep step : this.m_steps) {
			step.updateVariable(updateVar);
		}
		
	}

	public HAPMatchers discover(Map<String, HAPVariableInfo> variablesInfo, HAPDataTypeCriteria expectOutputCriteria, HAPProcessTaskContext context, HAPDataTypeHelper dataTypeHelper) {

		Map<String, HAPVariableInfo> varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		varsInfo.putAll(variablesInfo);
		Map<String, HAPVariableInfo> oldVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		Set<HAPDataTypeCriteria> exitCriterias;
		do {
			oldVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
			oldVarsInfo.putAll(varsInfo);
			context.clear();

			Map<String, HAPVariableInfo> localVariablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
			exitCriterias = new HashSet<HAPDataTypeCriteria>();
			for(HAPExecuteStep step : this.m_steps) {
				step.discover(variablesInfo, localVariablesInfo, exitCriterias, context, dataTypeHelper);
				if(!context.isSuccess())  break;
			}
		}while(!HAPBasicUtility.isEqualMaps(varsInfo, oldVarsInfo) && context.isSuccess());
		
		
		HAPMatchers varMatchers = dataTypeHelper.buildMatchers(new HAPDataTypeCriteriaOr(new ArrayList(exitCriterias)), expectOutputCriteria);
		return varMatchers;
	}
}
