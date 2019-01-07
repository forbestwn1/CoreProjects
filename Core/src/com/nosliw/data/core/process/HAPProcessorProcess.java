package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.data.core.script.context.HAPProcessorContext;

public class HAPProcessorProcess{

	public static HAPExecutableProcess process(
			HAPDefinitionProcess processDefinition, 
			String id, 
			HAPContextGroup parentContext, 
			Map<String, HAPDefinitionProcess> processDefinitionsContext,
			HAPProcessContext processContext,
			HAPManagerProcess processMan,
			HAPEnvContextProcessor envContextProcessor) {
		
		HAPExecutableProcess out = null;

		//merge context with parent
		HAPContextGroup context = HAPProcessorContext.process(processDefinition.getContext(), parentContext, null, envContextProcessor);
		
		HAPContextGroup oldContext;
		do {
			out = new HAPExecutableProcess(processDefinition, id);
			oldContext = context.cloneContextGroup();
			Map<String, HAPExecutableDataAssociationGroup> results = new LinkedHashMap<String, HAPExecutableDataAssociationGroup>();
			Map<String, HAPDefinitionActivity> activities = processDefinition.getActivities();
			for(String activityId : activities.keySet()) {
				HAPDefinitionActivity activity = activities.get(activityId);
				
				//start activity
				if(activity.getType().equals(HAPConstant.ACTIVITY_TYPE_START))    out.setStartActivityId(activityId);    
				
				//process activity
				HAPExecutableActivity activityExe = processMan.getPluginManager().getPlugin(activity.getType()).process(activity, activityId, out, context, results, processDefinitionsContext, processMan, envContextProcessor, processContext);
				out.addActivity(activityId, activityExe);
			}
			out.setContext(context);
		}while(false);
		//while(!context.equals(oldContext));
		
		return out;
	}
	
	public static HAPExecutableProcess process(
			String processId, 
			HAPDefinitionProcessSuite suite, 			
			HAPProcessContext processContext,
			HAPManagerProcess processMan,
			HAPEnvContextProcessor envContextProcessor) {
		String id = new HAPIdProcess(suite.getId(), processId).getId();
		return process(suite.getProcess(processId), id, suite.getContext(), suite.getProcesses(), processContext, processMan, envContextProcessor);
	}
	
}
