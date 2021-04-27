package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.process.resource.HAPResourceDefinitionProcess;
import com.nosliw.data.core.resource.HAPEntityWithResourceContext;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.data.core.structure.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.structure.value.HAPContextStructureValueDefinitionGroup;

public class HAPProcessorProcess{

	//process process in suite
	public static HAPExecutableProcess process(
			HAPResourceDefinitionProcess processDef,
			HAPContextStructureValueDefinitionGroup extraContext,
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
			HAPContextStructureValueDefinitionGroup extraContext,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		HAPExecutableProcess out = null;
		HAPContextStructureValueDefinitionGroup oldContext = null;
		do {
			if(oldContext==null)   oldContext = (HAPContextStructureValueDefinitionGroup)HAPUtilityComponent.processElementComponentContext((HAPResourceDefinitionProcess)process.getEntity(), extraContext, runtimeEnv, HAPUtilityConfigure.getContextProcessConfigurationForProcess()); 
			else oldContext = out.getContext();
			out = new HAPExecutableProcess((HAPResourceDefinitionProcess)process.getEntity(), id, processMan.getPluginManager());
			HAPProcessorProcess.process(out, oldContext, (HAPContextProcessor)process.getResourceContext(), serviceProviders, processMan, runtimeEnv, processTracker);
		}while(!oldContext.equals(out.getContext()));
		return out;
	}
	
	private static void process(
			HAPExecutableProcess out,
			HAPContextStructureValueDefinitionGroup originContext, 
			HAPContextProcessor processContext,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPRuntimeEnvironment runtimeEnv,
			HAPProcessTracker processTracker) {
		if(processContext==null)   processContext = HAPContextProcessor.createContext(runtimeEnv.getResourceDefinitionManager());
		HAPContextStructureValueDefinitionGroup context = originContext.cloneContextGroup();
		Map<String, HAPExecutableDataAssociation> results = new LinkedHashMap<String, HAPExecutableDataAssociation>();

		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(out.getDefinition().getAttachmentContainer(), serviceProviders, runtimeEnv.getServiceManager().getServiceDefinitionManager()); 
		
		for(String activityId : out.getDefinition().getProcess().getActivities().keySet()) {
			HAPDefinitionActivity activity = out.getDefinition().getProcess().getActivityById(activityId);
			
			//start activity
			if(activity.getType().equals(HAPConstantShared.ACTIVITY_TYPE_START))    out.setStartActivityId(activityId);    
			
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
