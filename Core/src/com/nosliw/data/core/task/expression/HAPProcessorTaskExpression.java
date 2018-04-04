package com.nosliw.data.core.task.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.task.HAPProcessorTask;
import com.nosliw.data.core.task.HAPManagerTask;
import com.nosliw.data.core.task.HAPUpdateVariableDomain;
import com.nosliw.data.core.task.HAPUpdateVariableMap;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPExecutableTask;

public class HAPProcessorTaskExpression implements HAPProcessorTask{

	private HAPManagerTaskExpression m_expressionTaskManager;
	
	private HAPManagerTask m_managerTask;
	
	public HAPProcessorTaskExpression(HAPManagerTaskExpression expressionTaskManager, HAPManagerTask managerTask) {
		this.m_managerTask = managerTask;
		this.m_expressionTaskManager = expressionTaskManager;
	}
	
	@Override
	public HAPExecutableTask process(HAPDefinitionTask taskDefinition, String domain, Map<String, String> variableMap, 
			Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants,
			HAPProcessContext context) {
		HAPDefinitionTaskExpression taskDefExp = (HAPDefinitionTaskExpression)taskDefinition; 
		
		HAPExecutableTaskExpression out = new HAPExecutableTaskExpression(taskDefExp, domain);

		//get constants for this task by merging constants from internal and external
		Map<String, HAPData> taskContextConstants = this.getContextConstantsForTask(taskDefExp, contextConstants);
		
		//process steps
		this.processSteps(out, taskDefExp, contextTaskDefinitions, taskContextConstants, context);

		//update variable in task executable : add domain prefix
		out.updateVariable(new HAPUpdateVariableDomain(domain));

		//process steps
		this.postProcessSteps(out, taskDefExp, contextTaskDefinitions, taskContextConstants, context);
		
		//process references info
		this.processReferencedTasks(out, taskDefExp, contextTaskDefinitions, contextConstants, context);
		this.updateReference(out);
		
		//get updated variables map according to domain
		Map<String, String> domainedVariableMap = new LinkedHashMap<String, String>();
		if(variableMap!=null) {
			for(String name : variableMap.keySet()) {
				domainedVariableMap.put(HAPExpressionUtility.buildFullVariableName(domain, name), variableMap.get(name));
			}
		}
		//update variable in task
		out.updateVariable(new HAPUpdateVariableMap(domainedVariableMap));
		
		return out;
	}
	
	private void processSteps(HAPExecutableTaskExpression out, HAPDefinitionTaskExpression taskDefExp, Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants, HAPProcessContext context) {
		//update constants according to constants in context and in task
		for(int i=0; i<taskDefExp.getSteps().length; i++) {
			HAPDefinitionStep stepDef = taskDefExp.getSteps()[i];
			HAPExecutableStep step = this.m_expressionTaskManager.processStep(stepDef, out, i, contextTaskDefinitions, contextConstants, context);
			out.addStep(step);
		}
	}

	private void postProcessSteps(HAPExecutableTaskExpression executableTask, HAPDefinitionTaskExpression taskDefExp, Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants, HAPProcessContext context) {
		//update constants according to constants in context and in task
		for(int i=0; i<executableTask.getSteps().size(); i++) {
			HAPExecutableStep stepExe = executableTask.getSteps().get(i);
			this.m_expressionTaskManager.postProcessStep(stepExe, taskDefExp.getSteps()[i], executableTask, i, contextTaskDefinitions, contextConstants, context);
		}
	}

	private Map<String, HAPData> getContextConstantsForTask(HAPDefinitionTaskExpression taskDefExp, Map<String, HAPData> contextConstants){
		Map<String, HAPData> out = new LinkedHashMap<String, HAPData>();
		out.putAll(contextConstants);
		out.putAll(taskDefExp.getConstants());
		return out;
	}

	private void updateReference(HAPExecutableTaskExpression out) {
		 for(HAPExecutableStep step : out.getSteps()) {
			 step.updateReferencedExecute(out.getReferencedExecute());
		 }
	}
	
	private void processReferencedTasks(HAPExecutableTaskExpression out, HAPDefinitionTaskExpression taskDefExp,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants,
			HAPProcessContext context) {

		//find all reference infos in task
		Map<String, HAPReferenceInfo> referencesInfo = new LinkedHashMap<String, HAPReferenceInfo>();
		for(HAPDefinitionStep step : taskDefExp.getSteps()) {
			for(String refName : step.getReferenceNames()) {
				referencesInfo.put(refName, null);
			}
		}
		referencesInfo.putAll(out.getReferencesInfo());
		
		for(String refName : referencesInfo.keySet()) {
			HAPReferenceInfo referenceInfo = referencesInfo.get(refName);
			
			//referenced task name, by default, use referenceName as task name
			String refTaskName = null;   
			if(referenceInfo!=null)		refTaskName = referenceInfo.getReference();
			if(refTaskName==null)  refTaskName = refName;
			
			HAPDefinitionTask refedTaskDef = contextTaskDefinitions.get(refTaskName);

			//domain name for reference
			String refDomain = HAPExpressionUtility.buildFullVariableName(out.getDomain(), refName); 

			//pare reference variable map
			Map<String, String> refVarMap = new LinkedHashMap<String, String>();   //variable mapping from parent to reference expression
			if(referenceInfo!=null)		refVarMap = referenceInfo.getVariablesMap();
		
			//process reference task
			HAPExecutableTask executable = m_managerTask.processTask(refedTaskDef, refDomain, HAPBasicUtility.reverseMapping(refVarMap), contextTaskDefinitions, contextConstants, context);
			out.addReferencedExecute(refName, executable);
			
		}
	}

}
