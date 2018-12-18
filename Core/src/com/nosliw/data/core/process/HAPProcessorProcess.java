package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPExpressionUtility;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextNodeRootRelative;

public class HAPProcessorProcess{

	public static HAPExecutableProcess process(
			HAPDefinitionProcess processDefinition, 
			String domain, 
			HAPContextGroup parentContext, 
			Map<String, HAPDefinitionProcess> processDefinitionsContext,
			HAPProcessContext processContext,
			HAPManagerProcess flowTaskMan) {
		HAPExecutableProcess out = new HAPExecutableProcess(processDefinition, domain);

		//get constants for this task by merging constants from internal and external
		Map<String, HAPData> taskContextConstants = getContextConstantsForTask(processDefinition, contextConstants);
		
		//process steps
		processSteps(out, processDefinition, processDefinitionsContext, taskContextConstants, processContext, flowTaskMan);

		//update variable in task executable : add domain prefix
		out.updateVariable(new HAPUpdateVariableDomain(domain));

		//process steps
		postProcessSteps(out, processDefinition, processDefinitionsContext, taskContextConstants, processContext, flowTaskMan);
		
		//process references info
		processReferencedTasks(out, processDefinition, processDefinitionsContext, contextConstants, processContext, flowTaskMan);
		updateReference(out);

		//get updated variables map according to domain
		Map<String, String> domainedVariableMap = new LinkedHashMap<String, String>();
		for(String varNameInRefTask : variableMap.getElements().keySet()) {
			String varPath = ((HAPContextNodeRootRelative)variableMap.getElement(varNameInRefTask)).getSubPath().getFullPath();
			domainedVariableMap.put(varNameInRefTask, HAPExpressionUtility.buildFullVariableName(domain, varPath));
		}

		//update variable in task
		out.updateVariable(new HAPUpdateVariableMap(domainedVariableMap));
		
		return out;
	}
	
	private static void updateReference(HAPExecutableProcess out) {
		 for(HAPExecutableActivity step : out.getSteps()) {
			 step.updateReferencedExecute(out.getReferencedExecute());
		 }
	}
	
	private static void processReferencedTasks(HAPExecutableProcess out, HAPDefinitionProcess taskDef,
			Map<String, HAPDefinitionProcess> contextTaskDefinitions, Map<String, HAPData> contextConstants,
			HAPProcessContext context, HAPManagerProcess flowTaskMan) {
/*
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
*/		
	}

	private static void processSteps(HAPExecutableProcess out, HAPDefinitionProcess taskDefinition, Map<String, HAPDefinitionProcess> contextTaskDefinitions, Map<String, HAPData> contextConstants, HAPProcessContext processContext, HAPManagerProcess flowTaskMan) {
		//update constants according to constants in context and in task
		for(int i=0; i<taskDefinition.getActivities().size(); i++) {
			HAPDefinitionActivity stepDef = taskDefinition.getActivities().get(i);
			HAPProcessorActivity stepProcessor = flowTaskMan.getActivityPlugin(stepDef.getType()).getActivityProcessor();
			HAPExecutableActivity step = stepProcessor.process(stepDef, out, i, stepDef.getName(), contextTaskDefinitions, contextConstants, processContext);
			out.addStep(step);
		}
	}

	private static void postProcessSteps(HAPExecutableProcess executableTask, HAPDefinitionProcess taskDefinition, Map<String, HAPDefinitionProcess> contextTaskDefinitions, Map<String, HAPData> contextConstants, HAPProcessContext context, HAPManagerProcess flowTaskMan) {
		//update constants according to constants in context and in task
		for(int i=0; i<executableTask.getSteps().size(); i++) {
			HAPExecutableActivity stepExe = executableTask.getSteps().get(i);
			HAPProcessorActivity stepProcessor = flowTaskMan.getActivityPlugin(stepExe.getType()).getActivityProcessor();
			stepProcessor.postProcess(stepExe, taskDefinition.getActivities().get(i), executableTask, i, taskDefinition.getActivities().get(i).getName(), contextTaskDefinitions, contextConstants, context);
		}
	}

	private static Map<String, HAPData> getContextConstantsForTask(HAPDefinitionProcess taskDefExp, Map<String, HAPData> contextConstants){
		Map<String, HAPData> out = new LinkedHashMap<String, HAPData>();
//		out.putAll(contextConstants);
//		out.putAll(taskDefExp.getConstants());
		return out;
	}
}
