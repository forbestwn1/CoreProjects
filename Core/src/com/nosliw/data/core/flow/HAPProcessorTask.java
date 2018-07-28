package com.nosliw.data.core.flow;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.context.HAPContext;
import com.nosliw.data.context.HAPContextNodeRootRelative;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpressionUtility;

public class HAPProcessorTask{

	public static HAPExecutableTask process(HAPDefinitionTask taskDefinition, String domain, HAPContext variableMap, 
			Map<String, HAPDefinitionTask> taskDefinitionsContext, Map<String, HAPData> contextConstants,
			HAPProcessContext processContext, HAPManagerFlowTask flowTaskMan) {
		HAPExecutableTask out = new HAPExecutableTask(taskDefinition, domain);

		//get constants for this task by merging constants from internal and external
		Map<String, HAPData> taskContextConstants = getContextConstantsForTask(taskDefinition, contextConstants);
		
		//process steps
		processSteps(out, taskDefinition, taskDefinitionsContext, taskContextConstants, processContext, flowTaskMan);

		//update variable in task executable : add domain prefix
		out.updateVariable(new HAPUpdateVariableDomain(domain));

		//process steps
		postProcessSteps(out, taskDefinition, taskDefinitionsContext, taskContextConstants, processContext, flowTaskMan);
		
		//process references info
		processReferencedTasks(out, taskDefinition, taskDefinitionsContext, contextConstants, processContext, flowTaskMan);
		updateReference(out);

		//get updated variables map according to domain
		Map<String, String> domainedVariableMap = new LinkedHashMap<String, String>();
		for(String varNameInRefTask : variableMap.getElements().keySet()) {
			String varPath = ((HAPContextNodeRootRelative)variableMap.getElement(varNameInRefTask)).getPath().getFullPath();
			domainedVariableMap.put(varNameInRefTask, HAPExpressionUtility.buildFullVariableName(domain, varPath));
		}

		//update variable in task
		out.updateVariable(new HAPUpdateVariableMap(domainedVariableMap));
		
		return out;
	}
	
	private static void updateReference(HAPExecutableTask out) {
		 for(HAPExecutableStep step : out.getSteps()) {
			 step.updateReferencedExecute(out.getReferencedExecute());
		 }
	}
	
	private static void processReferencedTasks(HAPExecutableTask out, HAPDefinitionTask taskDef,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants,
			HAPProcessContext context, HAPManagerFlowTask flowTaskMan) {

		//find all reference infos in task
		Map<String, HAPReferenceInfo> referencesInfo = new LinkedHashMap<String, HAPReferenceInfo>();
		for(HAPDefinitionStep step : taskDef.getSteps()) {
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

			//process reference task
			HAPExecutableTask executable = process(refedTaskDef, refDomain, referenceInfo.getVariablesMap(), contextTaskDefinitions, contextConstants, context, flowTaskMan);
			out.addReferencedExecute(refName, executable);
			
		}
	}

	private static void processSteps(HAPExecutableTask out, HAPDefinitionTask taskDefinition, Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants, HAPProcessContext processContext, HAPManagerFlowTask flowTaskMan) {
		//update constants according to constants in context and in task
		for(int i=0; i<taskDefinition.getSteps().size(); i++) {
			HAPDefinitionStep stepDef = taskDefinition.getSteps().get(i);
			HAPProcessorStep stepProcessor = flowTaskMan.getStepPlugin(stepDef.getType()).getStepProcessor();
			HAPExecutableStep step = stepProcessor.process(stepDef, out, i, stepDef.getName(), contextTaskDefinitions, contextConstants, processContext);
			out.addStep(step);
		}
	}

	private static void postProcessSteps(HAPExecutableTask executableTask, HAPDefinitionTask taskDefinition, Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants, HAPProcessContext context, HAPManagerFlowTask flowTaskMan) {
		//update constants according to constants in context and in task
		for(int i=0; i<executableTask.getSteps().size(); i++) {
			HAPExecutableStep stepExe = executableTask.getSteps().get(i);
			HAPProcessorStep stepProcessor = flowTaskMan.getStepPlugin(stepExe.getType()).getStepProcessor();
			stepProcessor.postProcess(stepExe, taskDefinition.getSteps().get(i), executableTask, i, taskDefinition.getSteps().get(i).getName(), contextTaskDefinitions, contextConstants, context);
		}
	}

	private static Map<String, HAPData> getContextConstantsForTask(HAPDefinitionTask taskDefExp, Map<String, HAPData> contextConstants){
		Map<String, HAPData> out = new LinkedHashMap<String, HAPData>();
		out.putAll(contextConstants);
		out.putAll(taskDefExp.getConstants());
		return out;
	}
}
