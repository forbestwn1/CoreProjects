package com.nosliw.data.core.task.expression;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPDataTypeHelper;

import com.nosliw.data.core.criteria.HAPDataTypeCriteriaOr;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.task.HAPExecuteTask;
import com.nosliw.data.core.task.HAPUpdateVariable;

public class HAPExecuteTaskExpression implements HAPExecuteTask{

	private HAPDefinitionTaskExpression m_taskDefinition;
	
	//unique in system
	private String m_id;
	
	private String m_domain;  //?? intermediate value

	//referenced task
	private Map<String, HAPExecuteTask> m_executeReferences;
	
	private List<HAPExecuteStep> m_steps;

	//variable info defined for task
	private Map<String, HAPVariableInfo> m_varsInfo;
	
	//
	private HAPVariableInfo m_output;
	
	// store all the matchers from variables info to variables defined in task
	private Map<String, HAPMatchers> m_varsMatchers;
	
	public HAPExecuteTaskExpression(HAPDefinitionTaskExpression taskDef, String domain) {
		this.m_taskDefinition = taskDef;
		this.m_domain = domain;
		this.m_varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		this.m_varsInfo.putAll(taskDef.getVariables());
	}
	
	public void setId(String id) {  this.m_id = id;   }
	
	public void addReferencedExecute(String refName, HAPExecuteTask execute) {	this.m_executeReferences.put(refName, execute);	}
	public Map<String, HAPExecuteTask> getReferencedExecute(){  return this.m_executeReferences;    }
	
	public void addStep(HAPExecuteStep step) {  this.m_steps.add(step);   }
	public List<HAPExecuteStep> getSteps(){   return this.m_steps;   }
	
	public String getDomain() {  return this.m_domain;  }

	@Override
	public Map<String, HAPMatchers> getVariableMatchers() {	return this.m_varsMatchers;	}
	
	@Override
	public HAPVariableInfo getOutput() {  return this.m_output;  }

	@Override
	public void updateVariable(HAPUpdateVariable updateVar) {
		//update variables in variable info def
		Map<String, HAPVariableInfo> updatedVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		for(String varName : this.m_varsInfo.keySet()) {
			updatedVarsInfo.put(updateVar.getUpdatedVariable(varName), this.m_varsInfo.get(varName));
		}
		this.m_varsInfo = updatedVarsInfo;
		
		//update variables in steps
		for(HAPExecuteStep step : this.m_steps) {
			step.updateVariable(updateVar);
		}
	}

	@Override
	public HAPMatchers discoverVariable(Map<String, HAPVariableInfo> parentVariablesInfo, HAPVariableInfo expectOutputCriteria, HAPProcessContext context, HAPDataTypeHelper dataTypeHelper) {
		Map<String, HAPVariableInfo> varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		varsInfo.putAll(parentVariablesInfo);

		Map<String, HAPVariableInfo> oldVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		Set<HAPVariableInfo> exitCriterias;
		do {
			oldVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
			oldVarsInfo.putAll(varsInfo);
			context.clear();

			Map<String, HAPVariableInfo> localVariablesInfo = new LinkedHashMap<String, HAPVariableInfo>();
			exitCriterias = new HashSet<HAPVariableInfo>();
			for(HAPExecuteStep step : this.m_steps) {
				step.discover(parentVariablesInfo, localVariablesInfo, exitCriterias, context, dataTypeHelper);
				if(!context.isSuccess())  break;
			}
		}while(!HAPBasicUtility.isEqualMaps(varsInfo, oldVarsInfo) && context.isSuccess());

		
		//cal variable matchers, update parent variable
		for(String varName : this.m_varsInfo.keySet()){
			HAPVariableInfo varInfo = this.m_varsInfo.get(varName);
			HAPVariableInfo parentVarInfo = varsInfo.get(varName);
			if(parentVarInfo==null){
				parentVarInfo = new HAPVariableInfo();
				parentVarInfo.setCriteria(varInfo.getCriteria());
				this.m_varsInfo.put(varName, parentVarInfo);
			}
			else{
				if(parentVarInfo.getStatus().equals(HAPConstant.EXPRESSION_VARIABLE_STATUS_OPEN)){
					HAPVariableInfo adjustedCriteria = dataTypeHelper.merge(varInfo.getCriteria(), parentVarInfo.getCriteria());
					parentVarInfo.setCriteria(adjustedCriteria);
				}
			}

			//cal var converters
			HAPMatchers varMatchers = dataTypeHelper.buildMatchers(parentVarInfo.getCriteria(), varInfo.getCriteria());
			this.m_varsMatchers.put(varName, varMatchers);
		}

		//output
		this.m_output = new HAPDataTypeCriteriaOr(new ArrayList(exitCriterias));
		return dataTypeHelper.buildMatchers(this.m_output, expectOutputCriteria);
	}

	@Override
	public List<HAPResourceId> discoverResources() {
		Set<HAPResourceId> out = new HashSet<HAPResourceId>();
		//resource for steps
		for(HAPExecuteStep step : this.m_steps) {
			out.addAll(step.discoverResources());
		}
		//resources from task definition
		Set<HAPResourceDependent> required = this.m_taskDefinition.getRequiredResources();
		for(HAPResourceDependent dep : required) {
			out.add(dep.getId());
		}
		return new ArrayList(out);
	}

}
