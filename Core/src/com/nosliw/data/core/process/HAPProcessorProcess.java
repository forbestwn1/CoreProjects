package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.erro.HAPErrorUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPDefinitionDataAssociationGroup;
import com.nosliw.data.core.script.context.HAPExecutableDataAssociationGroup;
import com.nosliw.data.core.script.context.HAPExecutableDataAssociationGroupWithTarget;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPProcessorDataAssociation;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;

public class HAPProcessorProcess{

	public static HAPExecutableEmbededProcess process(
			HAPDefinitionEmbededProcess embededProcessDefinition,
			String id, 
			HAPContextGroup parentContext, 
			Map<String, HAPDefinitionProcess> localProcesses,
			HAPManagerProcess processMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPExecutableEmbededProcess out = null;
		HAPContextGroup oldContext = null;
		do {
			if(oldContext==null)   oldContext = HAPProcessorContext.process(embededProcessDefinition.getContext(), parentContext, null, contextProcessRequirement);
			else oldContext = out.getContext();
			out = new HAPExecutableEmbededProcess(embededProcessDefinition, id);
			HAPProcessorProcess.process(out, oldContext, localProcesses, processMan, contextProcessRequirement, processTracker);
			
			Map<String, HAPDefinitionDataAssociationGroup> outputMapping = embededProcessDefinition.getOutputMapping();
			for(String result : outputMapping.keySet()) {
				HAPExecutableDataAssociationGroupWithTarget backToGlobalContext = HAPProcessorDataAssociation.processDataAssociation(out.getResult(result).getContext().getContext(), outputMapping.get(result), out.getContext(), true, contextProcessRequirement); 
				out.addBackToGlobalContext(result, backToGlobalContext);
			}
			
		}while(!oldContext.equals(out.getContext()));
		return out;
	}
	
	//
	public static HAPExecutableProcess process(
			HAPDefinitionProcess processDefinition, 
			String id, 
			HAPContextGroup parentContext, 
			Map<String, HAPDefinitionProcess> localProcesses,
			HAPManagerProcess processMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPExecutableProcess out = null;
		HAPContextGroup oldContext = null;
		do {
			if(oldContext==null)   oldContext = HAPProcessorContext.process(processDefinition.getContext(), parentContext, null, contextProcessRequirement);
			else oldContext = out.getContext();
			out = new HAPExecutableProcess(processDefinition, id);
			HAPProcessorProcess.process(out, oldContext, localProcesses, processMan, contextProcessRequirement, processTracker);
		}while(!oldContext.equals(out.getContext()));
		return out;
	}

	private static void process(
			HAPExecutableProcess out,
			HAPContextGroup originContext, 
			Map<String, HAPDefinitionProcess> localProcesses,
			HAPManagerProcess processMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPContextGroup context = originContext.cloneContextGroup();
		Map<String, HAPExecutableDataAssociationGroup> results = new LinkedHashMap<String, HAPExecutableDataAssociationGroup>();
		for(String activityId : out.getDefinition().getActivities().keySet()) {
			HAPDefinitionActivity activity = out.getDefinition().getActivityById(activityId);
			
			//start activity
			if(activity.getType().equals(HAPConstant.ACTIVITY_TYPE_START))    out.setStartActivityId(activityId);    
			
			//process activity
			HAPExecutableActivity activityExe = processMan.getPluginManager().getPlugin(activity.getType()).process(activity, activityId, out, context, results, localProcesses, processMan, contextProcessRequirement, processTracker);
			if(activityExe!=null) {
				out.addActivity(activityId, activityExe);
			}
			else {
				HAPErrorUtility.warning("activity is not processed");
			}
		}
		out.setContext(context);
		//process results
		out.setResults(results);
	}

	//process process in suite
	public static HAPExecutableProcess process(
			String processId, 
			HAPDefinitionProcessSuite suite, 			
			HAPManagerProcess processMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		String id = new HAPIdProcess(suite.getId(), processId).getId();
		return process(suite.getProcess(processId), id, suite.getContext(), suite.getProcesses(), processMan, contextProcessRequirement, processTracker);
	}
}
