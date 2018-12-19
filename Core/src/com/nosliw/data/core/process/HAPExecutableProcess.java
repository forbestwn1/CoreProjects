package com.nosliw.data.core.process;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
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

@HAPEntityWithAttribute
public class HAPExecutableProcess extends HAPSerializableImp{

	public static final String INFO_LOCALVRIABLE = "localVariable";
	
	private HAPDefinitionProcess m_taskDefinition;
	
	//unique in system
	private String m_id;
	
	private String m_domain;  //?? intermediate value

	//referenced task
	private Map<String, HAPExecutableProcess> m_executeReferences;
	
	private List<HAPExecutableActivity> m_steps;
	private Map<String, HAPExecutableActivity> m_stepsByName;

	//variable info defined for task
	private Map<String, HAPVariableInfo> m_varsInfo;

	
	private Map<String, HAPReferenceInfo> m_referencesInfo;
	
	//
	private HAPDataTypeCriteria m_output;
	
	// store all the matchers from variables info to variables defined in task
	private Map<String, HAPMatchers> m_varsMatchers;
	
	public HAPExecutableProcess(HAPDefinitionProcess taskDef, String domain) {
//		this.m_taskDefinition = taskDef;
//		this.m_domain = domain;
//		this.m_steps = new ArrayList<HAPExecutableStep>();
//		this.m_stepsByName = new LinkedHashMap<String, HAPExecutableStep>();
//		this.m_varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
//		this.m_varsInfo.putAll(taskDef.getDataContext());
//		this.m_executeReferences = new LinkedHashMap<String, HAPExecutableTask>();
//		this.m_varsMatchers = new LinkedHashMap<String, HAPMatchers>();
//		
//		this.m_referencesInfo = new LinkedHashMap<String, HAPReferenceInfo>();
//		for(String refName : taskDef.getReferences().keySet()) {
//			this.m_referencesInfo.put(refName, taskDef.getReferences().get(refName).clone());
//		}
	}
	
	public void setId(String id) {  this.m_id = id;   }
	
	public void addReferencedExecute(String refName, HAPExecutableProcess execute) {	this.m_executeReferences.put(refName, execute);	}
	public Map<String, HAPExecutableProcess> getReferencedExecute(){  return this.m_executeReferences;    }
	
	public void addStep(HAPExecutableActivity step) {  
		this.m_steps.add(step);
		String stepName = step.getName();
		if(HAPBasicUtility.isStringNotEmpty(stepName)) {
			this.m_stepsByName.put(stepName, step);
		}
	}
	public List<HAPExecutableActivity> getSteps(){   return this.m_steps;   }
	public HAPExecutableActivity getStep(int index) {
		if(index>=this.m_steps.size())  return null;
		return this.m_steps.get(index);   
	}
	public HAPExecutableActivity getStep(String name) {  return this.m_stepsByName.get(name);   }
	
	public String getDomain() {  return this.m_domain;  }

	public String getName() {  return this.m_taskDefinition.getName();   }
	
	public Map<String, HAPMatchers> getVariableMatchers() {	return this.m_varsMatchers;	}
	
	public Map<String, HAPReferenceInfo> getReferencesInfo(){   return this.m_referencesInfo;    }
	
	public Set<String> getReferences() {  return this.m_executeReferences.keySet();  }

	public Set<String> getVariables() {  return this.m_varsInfo.keySet();  }

	public HAPDataTypeCriteria getOutput() {  return this.m_output;  }

	public void updateVariable(HAPUpdateVariable updateVar) {
		//update variables in variable info def
		Map<String, HAPVariableInfo> updatedVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		for(String varName : this.m_varsInfo.keySet()) {
			updatedVarsInfo.put(updateVar.getUpdatedVariable(varName), this.m_varsInfo.get(varName));
		}
		this.m_varsInfo = updatedVarsInfo;
		
		//update variables in steps
		for(HAPExecutableActivity step : this.m_steps) {
			step.updateVariable(updateVar);
		}
		
		//update variables in referenced task
		for(String refName : this.m_executeReferences.keySet()) {
			this.m_executeReferences.get(refName).updateVariable(updateVar);
		}
		
		for(String refName : this.m_referencesInfo.keySet()) {
			this.m_referencesInfo.get(refName).upateVariableName(updateVar);
		}
	}

	public void discoverVariable(Map<String, HAPVariableInfo> parentVariablesInfo, HAPDataTypeCriteria expectOutputCriteria, HAPProcessContext context) {
		Map<String, HAPVariableInfo> varsInfo = new LinkedHashMap<String, HAPVariableInfo>();
		varsInfo.putAll(parentVariablesInfo);

		Map<String, HAPVariableInfo> oldVarsInfo = null;
		do {
			oldVarsInfo = new LinkedHashMap<String, HAPVariableInfo>();
			oldVarsInfo.putAll(varsInfo);
			context.clear();

			for(HAPExecutableActivity step : this.m_steps) {
				step.discoverVariable(varsInfo, expectOutputCriteria, context);
				if(!context.isSuccess())  break;
			}

			//remove local variables
			Set<String> localVars = new HashSet<String>();
			for(String varName : varsInfo.keySet()) {
				if(oldVarsInfo.get(varName)==null) {
					if(HAPBasicUtility.isStringNotEmpty(varsInfo.get(varName).getInfoValue(HAPExecutableProcess.INFO_LOCALVRIABLE)))  localVars.add(varName);
				}
			}
			for(String localVar : localVars)   varsInfo.remove(localVar);
			
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
			HAPExecutableActivity step = this.m_steps.get(i);
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

	public List<HAPResourceDependent> getResourceDependency(){  return this.m_resourceDependency;  }

	
	public List<HAPResourceId> getResourceDependency1() {
		Set<HAPResourceId> out = new HashSet<HAPResourceId>();
		//resource for steps
		for(HAPExecutableActivity step : this.m_steps) {
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
