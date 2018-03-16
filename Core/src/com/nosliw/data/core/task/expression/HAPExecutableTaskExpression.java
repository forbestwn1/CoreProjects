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
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPDataTypeCriteriaOr;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.task.HAPExecutableTask;
import com.nosliw.data.core.task.HAPUpdateVariable;

public class HAPExecutableTaskExpression implements HAPExecutableTask{

	public static final String INFO_LOCALVRIABLE = "localVariable";
	
	private HAPDefinitionTaskExpression m_taskDefinition;
	
	//unique in system
	private String m_id;
	
	private String m_domain;  //?? intermediate value

	//referenced task
	private Map<String, HAPExecutableTask> m_executeReferences;
	
	private List<HAPExecutableStep> m_steps;
	private Map<String, HAPExecutableStep> m_stepsByName;

	//variable info defined for task
	private Map<String, HAPVariableInfo> m_varsInfo;
	
	//
	private HAPDataTypeCriteria m_output;
	
	// store all the matchers from variables info to variables defined in task
	private Map<String, HAPMatchers> m_varsMatchers;
	
	public HAPExecutableTaskExpression(HAPDefinitionTaskExpression taskDef, String domain) {
		this.m_taskDefinition = taskDef;
		this.m_domain = domain;
		this.m_steps = new ArrayList<HAPExecutableStep>();
		this.m_stepsByName = new LinkedHashMap<String, HAPExecutableStep>();
		this.m_varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		this.m_varsInfo.putAll(taskDef.getVariables());
	}
	
	public void setId(String id) {  this.m_id = id;   }
	
	public void addReferencedExecute(String refName, HAPExecutableTask execute) {	this.m_executeReferences.put(refName, execute);	}
	public Map<String, HAPExecutableTask> getReferencedExecute(){  return this.m_executeReferences;    }
	
	public void addStep(HAPExecutableStep step) {  
		this.m_steps.add(step);
		String stepName = step.getName();
		if(HAPBasicUtility.isStringNotEmpty(stepName)) {
			this.m_stepsByName.put(stepName, step);
		}
	}
	public List<HAPExecutableStep> getSteps(){   return this.m_steps;   }
	public HAPExecutableStep getStep(int index) {
		if(index>=this.m_steps.size())  return null;
		return this.m_steps.get(index);   
	}
	public HAPExecutableStep getStep(String name) {  return this.m_stepsByName.get(name);   }
	
	public String getDomain() {  return this.m_domain;  }

	public Map<String, HAPMatchers> getVariableMatchers() {	return this.m_varsMatchers;	}
	
	@Override
	public String getType() {	return this.m_taskDefinition.getType();	}

	@Override
	public Set<String> getReferences() {  return this.m_executeReferences.keySet();  }

	@Override
	public Set<String> getVariables() {  return this.m_varsInfo.keySet();  }

	@Override
	public HAPDataTypeCriteria getOutput() {  return this.m_output;  }

	@Override
	public void updateVariable(HAPUpdateVariable updateVar) {
		//update variables in variable info def
		Map<String, HAPVariableInfo> updatedVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		for(String varName : this.m_varsInfo.keySet()) {
			updatedVarsInfo.put(updateVar.getUpdatedVariable(varName), this.m_varsInfo.get(varName));
		}
		this.m_varsInfo = updatedVarsInfo;
		
		//update variables in steps
		for(HAPExecutableStep step : this.m_steps) {
			step.updateVariable(updateVar);
		}
	}

	@Override
	public void discoverVariable(Map<String, HAPVariableInfo> parentVariablesInfo, HAPDataTypeCriteria expectOutputCriteria, HAPProcessContext context) {
		Map<String, HAPVariableInfo> varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		varsInfo.putAll(parentVariablesInfo);

		Map<String, HAPVariableInfo> oldVarsInfo = null;
		do {
			oldVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
			oldVarsInfo.putAll(varsInfo);
			context.clear();

			for(HAPExecutableStep step : this.m_steps) {
				step.discoverVariable(varsInfo, expectOutputCriteria, context);
				if(!context.isSuccess())  break;
			}

			//remove local variables
			Set<String> localVars = new HashSet<String>();
			for(String varName : varsInfo.keySet()) {
				if(HAPBasicUtility.isStringNotEmpty(varsInfo.get(varName).getInfoValue(HAPExecutableTaskExpression.INFO_LOCALVRIABLE)))  localVars.add(varName);
			}
			for(String localVar : localVars)   localVars.remove(localVar);
			
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
					HAPDataTypeCriteria adjustedCriteria = HAPExpressionManager.dataTypeHelper.merge(varInfo.getCriteria(), parentVarInfo.getCriteria());
					parentVarInfo.setCriteria(adjustedCriteria);
				}
			}

			//cal var converters
			HAPMatchers varMatchers = HAPExpressionManager.dataTypeHelper.buildMatchers(parentVarInfo.getCriteria(), varInfo.getCriteria());
			this.m_varsMatchers.put(varName, varMatchers);
		}

		//output
		Set<HAPDataTypeCriteria> exitCriterias = new HashSet<HAPDataTypeCriteria>();
		for(int i=0; i<this.m_steps.size(); i++) {
			HAPExecutableStep step = this.m_steps.get(i);
			if(i>=this.m_steps.size()-1) {
				//last one in step
				exitCriterias.add(step.getOutput());
			}
			else {
				HAPDataTypeCriteria exitCriteria = step.getExitDataTypeCriteria();
				if(exitCriteria!=null)  exitCriterias.add(exitCriteria);
			}
		}
		this.m_output = new HAPDataTypeCriteriaOr(new ArrayList(exitCriterias));
	}

	@Override
	public List<HAPResourceId> getResourceDependency() {
		Set<HAPResourceId> out = new HashSet<HAPResourceId>();
		//resource for steps
		for(HAPExecutableStep step : this.m_steps) {
			out.addAll(step.getResourceDependency());
		}
		//resources from task definition
		Set<HAPResourceDependent> required = this.m_taskDefinition.getRequiredResources();
		for(HAPResourceDependent dep : required) {
			out.add(dep.getId());
		}
		return new ArrayList(out);
	}

}
