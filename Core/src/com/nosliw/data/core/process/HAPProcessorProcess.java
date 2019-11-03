package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;

public class HAPProcessorProcess{

	//process process in suite
	public static HAPExecutableProcess process(
			String processId, 
			HAPDefinitionProcessSuite suite, 			
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcessDefinition processMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		String id = new HAPIdProcess(suite.getId(), processId).getId();
		HAPContextProcessor processContext = HAPContextProcessor.createContext(suite, processMan);
		return process(id, new HAPDefinitionProcessWithContext(suite.getProcess(processId), processContext), suite.getContext(), serviceProviders, processMan, contextProcessRequirement, processTracker);
	}

	public static HAPExecutableProcess process(
			String id, 
			HAPDefinitionProcessWithContext process,
			HAPContextGroup parentContext, 
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcessDefinition processMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPExecutableProcess out = null;
		HAPContextGroup oldContext = null;
		do {
			if(oldContext==null)   oldContext = HAPProcessorContext.process(process.getProcess().getContext(), HAPParentContext.createDefault(parentContext), HAPUtilityConfigure.getContextProcessConfigurationForProcess(), contextProcessRequirement);
			else oldContext = out.getContext();
			out = new HAPExecutableProcess(process.getProcess(), id);
			HAPProcessorProcess.process(out, oldContext, process.getContext(), serviceProviders, processMan, contextProcessRequirement, processTracker);
		}while(!oldContext.equals(out.getContext()));
		return out;
	}

	
	private static void process(
			HAPExecutableProcess out,
			HAPContextGroup originContext, 
			HAPContextProcessor processContext,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcessDefinition processMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		if(processContext==null)   processContext = HAPContextProcessor.createContext(processMan);
		HAPContextGroup context = originContext.cloneContextGroup();
		Map<String, HAPExecutableDataAssociation> results = new LinkedHashMap<String, HAPExecutableDataAssociation>();

		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(serviceProviders, out.getDefinition(), contextProcessRequirement.serviceDefinitionManager); 
		
		for(String activityId : out.getDefinition().getActivities().keySet()) {
			HAPDefinitionActivity activity = out.getDefinition().getActivityById(activityId);
			
			//start activity
			if(activity.getType().equals(HAPConstant.ACTIVITY_TYPE_START))    out.setStartActivityId(activityId);    
			
			//process activity
			HAPExecutableActivity activityExe = processMan.getPluginManager().getPlugin(activity.getType()).process(activity, activityId, processContext, out, context, results, allServiceProviders, processMan, contextProcessRequirement, HAPUtilityConfigure.getContextProcessConfigurationForActivity(), processTracker);
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

}
