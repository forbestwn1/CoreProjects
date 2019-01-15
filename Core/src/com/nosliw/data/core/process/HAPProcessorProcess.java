package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPProcessorContext;

public class HAPProcessorProcess{

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

		//merge context with parent
		HAPContextGroup context = HAPProcessorContext.process(processDefinition.getContext(), parentContext, null, contextProcessRequirement);
		Map<String, HAPExecutableDataAssociationGroup> results;
		HAPContextGroup oldContext;
		do {
			oldContext = context.cloneContextGroup();
			out = new HAPExecutableProcess(processDefinition, id);
			results = new LinkedHashMap<String, HAPExecutableDataAssociationGroup>();
			for(String activityId : processDefinition.getActivities().keySet()) {
				HAPDefinitionActivity activity = processDefinition.getActivityById(activityId);
				
				//start activity
				if(activity.getType().equals(HAPConstant.ACTIVITY_TYPE_START))    out.setStartActivityId(activityId);    
				
				//process activity
				HAPExecutableActivity activityExe = processMan.getPluginManager().getPlugin(activity.getType()).process(activity, activityId, out, context, results, localProcesses, processMan, contextProcessRequirement, processTracker);
				out.addActivity(activityId, activityExe);
			}
			out.setContext(context);
//		}while(false);
		}while(!context.equals(oldContext));
		
		//process results
		out.setResults(results);
		
		return out;
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
