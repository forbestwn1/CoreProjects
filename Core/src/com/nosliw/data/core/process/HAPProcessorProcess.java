package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;

public class HAPProcessorProcess{

	//process process in suite
	public static HAPExecutableProcess process(
			HAPDefinitionProcess processDef,
			HAPContextGroup extraContext,
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
			HAPContextGroup extraContext,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPExecutableProcess out = null;
		HAPContextGroup oldContext = null;
		do {
			if(oldContext==null)   oldContext = (HAPContextGroup)HAPUtilityComponent.processElementComponentContext((HAPDefinitionProcess)process.getEntity(), extraContext, runtimeEnv, HAPUtilityConfigure.getContextProcessConfigurationForProcess()); 
			else oldContext = out.getContext();
			out = new HAPExecutableProcess((HAPDefinitionProcess)process.getEntity(), id, processMan.getPluginManager());
			HAPProcessorProcess.process(out, oldContext, (HAPContextProcessor)process.getResourceContext(), serviceProviders, processMan, runtimeEnv, processTracker);
		}while(!oldContext.equals(out.getContext()));
		return out;
	}
	
	private static void process(
			HAPExecutableProcess out,
			HAPContextGroup originContext, 
			HAPContextProcessor processContext,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		if(processContext==null)   processContext = HAPContextProcessor.createContext(runtimeEnv.getResourceDefinitionManager());
		HAPContextGroup context = originContext.cloneContextGroup();
		Map<String, HAPExecutableDataAssociation> results = new LinkedHashMap<String, HAPExecutableDataAssociation>();

		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(out.getDefinition().getAttachmentContainer(), serviceProviders, runtimeEnv.getServiceManager().getServiceDefinitionManager()); 
		
		for(String activityId : out.getDefinition().getProcess().getActivities().keySet()) {
			HAPDefinitionActivity activity = out.getDefinition().getProcess().getActivityById(activityId);
			
			//start activity
			if(activity.getType().equals(HAPConstant.ACTIVITY_TYPE_START))    out.setStartActivityId(activityId);    
			
			//process activity
			HAPExecutableActivity activityExe = processMan.getPluginManager().getPlugin(activity.getType()).process(activity, activityId, processContext, out, context, results, allServiceProviders, processMan, runtimeEnv, HAPUtilityConfigure.getContextProcessConfigurationForActivity(), processTracker);
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
