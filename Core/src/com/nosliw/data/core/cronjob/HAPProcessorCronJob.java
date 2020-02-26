package com.nosliw.data.core.cronjob;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.attachment.HAPAttachmentUtility;
import com.nosliw.data.core.dataable.HAPDefinitionDataable;
import com.nosliw.data.core.dataable.HAPExecutableDataable;
import com.nosliw.data.core.dataable.HAPManagerDataable;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;

public class HAPProcessorCronJob {

	public static HAPExecutableCronJob process(
			HAPDefinitionCronJob cronJobDefinition,
			String id,
			HAPContextGroup parentContext, 
			HAPManagerProcess processMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionManager,
			HAPManagerServiceDefinition serviceDefinitionManager,
			HAPManagerResourceDefinition resourceDefMan,
			HAPManagerDataable dataableMan,
			HAPManagerScheduleDefinition scheduleMan) {

		HAPExecutableCronJob out = new HAPExecutableCronJob(cronJobDefinition, id);

		HAPRequirementContextProcessor contextProcessRequirement = new HAPRequirementContextProcessor(dataTypeHelper, runtime, expressionManager, serviceDefinitionManager, null);
		HAPConfigureContextProcessor contextProcessConfg = HAPUtilityConfiguration.getContextProcessConfigurationForCronJob();
		HAPProcessTracker processTracker = new HAPProcessTracker(); 

		//service providers
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(cronJobDefinition.getAttachmentContainer(), null, contextProcessRequirement.serviceDefinitionManager); 

		//process context 
		out.setContextGroup(HAPProcessorContext.process(cronJobDefinition.getContext(), HAPParentContext.createDefault(parentContext==null?new HAPContextGroup():parentContext), contextProcessConfg, contextProcessRequirement));
		
		//process process suite
		HAPDefinitionProcessSuite processSuite = HAPUtilityComponent.getProcessSuite(cronJobDefinition, processMan.getPluginManager()).clone(); 
		processSuite.setContext(out.getContext());   //kkk
//		processSuite.setContext(HAPProcessorContext.process(processSuite.getContext(), HAPParentContext.createDefault(out.getContext()), contextProcessConfg, contextProcessRequirement));
		out.setProcessSuite(processSuite);

		//process task
		HAPDefinitionProcess processDef = out.getProcessDefinition(cronJobDefinition.getTask().getTaskDefinition());
		HAPExecutableProcess processExe = HAPProcessorProcess.process(processDef, null, allServiceProviders, processMan, contextProcessRequirement, processTracker);
		HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(cronJobDefinition.getTask(), processExe, HAPParentContext.createDefault(out.getContext()), null, contextProcessRequirement);			
		out.setTask(processExeWrapper);
 
		//process end
		HAPDefinitionEnd endDef = cronJobDefinition.getEnd();
		HAPDefinitionDataable resourceDef = HAPAttachmentUtility.getResourceDefinition(cronJobDefinition.getAttachmentContainer(), endDef.getAttachmentReference().getType(), endDef.getAttachmentReference().getName(), resourceDefMan);
		HAPExecutableDataable exetable = dataableMan.process(resourceDef);
		
		HAPExecutableWrapperTask endExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(endDef.getEmbeded(), exetable, HAPParentContext.createDefault(out.getContext()), null, contextProcessRequirement);			
		out.setEnd(endExeWrapper);
	
		//process schedule
		HAPExecutablePollSchedule schedule = scheduleMan.parsePollSchedule(cronJobDefinition.getSchedule().getDefinition());
		out.setSchedule(schedule);
		
		return out;
	}
	
	
	
}
