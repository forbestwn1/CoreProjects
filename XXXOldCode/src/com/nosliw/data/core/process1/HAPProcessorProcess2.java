package com.nosliw.data.core.process1;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcess;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;

public class HAPProcessorProcess2{

	//process process in suite
	public static HAPExecutableProcess process(
			HAPResourceDefinitionProcess processDef,
			HAPValueStructureDefinitionGroup extraContext,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		String id = processDef.getElementId(); 
		HAPContextProcessor processContext = HAPContextProcessor.createContext(processDef.getSuite(), runtimeEnv.getResourceDefinitionManager());
		return process(id, new HAPEntityWithResourceContext(processDef, processContext), extraContext, serviceProviders, processMan, runtimeEnv, processTracker);
	}

	public static HAPExecutableProcess process(
			String id, 
			HAPEntityWithResourceContext process,
			HAPValueStructureDefinitionGroup extraContext,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPExecutableProcess out = null;
		HAPValueStructureDefinitionGroup oldContext = null;
		do {
			if(oldContext==null)   oldContext = (HAPValueStructureDefinitionGroup)HAPUtilityComponent.processElementComponentContext((HAPResourceDefinitionProcess)process.getEntity(), extraContext, runtimeEnv, HAPUtilityConfigure.getContextProcessConfigurationForProcess()); 
			else oldContext = out.getContext();
			out = new HAPExecutableProcess((HAPResourceDefinitionProcess)process.getEntity(), id, processMan.getPluginManager());
			HAPProcessorProcess2.process(out, oldContext, (HAPContextProcessor)process.getResourceContext(), serviceProviders, processMan, runtimeEnv, processTracker);
		}while(!oldContext.equals(out.getContext()));
		return out;
	}
	
	private static void process(
			HAPExecutableProcess out,
			HAPValueStructureDefinitionGroup originContext, 
			HAPContextProcessor processContext,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		if(processContext==null)   processContext = HAPContextProcessor.createContext(runtimeEnv.getResourceDefinitionManager());
		HAPValueStructureDefinitionGroup context = originContext.cloneValueStructureGroup();
		Map<String, HAPExecutableDataAssociation> results = new LinkedHashMap<String, HAPExecutableDataAssociation>();

		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(out.getTask().getAttachmentContainer(), serviceProviders, runtimeEnv.getServiceManager().getServiceDefinitionManager()); 
		
		for(String activityId : out.getTask().getProcess().getActivities().keySet()) {
			HAPDefinitionActivity activity = out.getTask().getProcess().getActivityById(activityId);
			
			//start activity
			if(activity.getActivityType().equals(HAPConstantShared.ACTIVITY_TYPE_START))    out.setStartActivityId(activityId);    
			
			//process activity
			HAPExecutableActivity activityExe = processMan.getPluginManager().getPlugin(activity.getActivityType()).process(activity, activityId, processContext, out, context, results, allServiceProviders, processMan, runtimeEnv, HAPUtilityConfigure.getContextProcessConfigurationForActivity(), processTracker);
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
