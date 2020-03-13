package com.nosliw.data.core.event;

import java.util.Map;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expression.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;

public class HAPProcessorEventTask {

	public static HAPExecutableEventTask process(
			HAPDefinitionEventTask definition, 
			HAPManagerProcess processMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionManager,
			HAPManagerServiceDefinition serviceDefinitionManager) {
		
		HAPRequirementContextProcessor contextProcessRequirement = new HAPRequirementContextProcessor(dataTypeHelper, runtime, expressionManager, serviceDefinitionManager, null);
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(definition.getAttachmentContainer(), null, contextProcessRequirement.serviceDefinitionManager); 

		HAPExecutableEventTask out = new HAPExecutableEventTask(definition);

		HAPDefinitionEventSource source = definition.getEventSourceInfo();
		
//		//poll task
//		HAPDefinitionPollTask pollTaskDef = source.getPollTask();
//		HAPDefinitionProcessSuiteElementEntity pollProcessDef = HAPUtilityProcess.getProcessDefinitionElementFromAttachment(pollTaskDef.getProcess().getTaskDefinition(), definition.getAttachmentContainer(), processMan.getPluginManager());
//		HAPExecutableProcess pollProcessExe = HAPProcessorProcess.process(null, new HAPDefinitionProcessWithContext(pollProcessDef), definition.getContext(), allServiceProviders, processMan, contextProcessRequirement, new HAPProcessTracker());
//		HAPExecutableWrapperTask pollProcessExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(pollTaskDef.getProcess(), pollProcessExe, HAPParentContext.createDefault(definition.getContext()), null, contextProcessRequirement);			
//		HAPExecutablePollTask pollTask = new HAPExecutablePollTask(source.getPollTask());
//		pollTask.setProcess(pollProcessExeWrapper);
//		out.setPollTask(pollTask);
//
//		//event handler
//		HAPDefinitionEventHandle eventHandleDef = definition.getEventHandle();
//		HAPDefinitionProcessSuiteElementEntity eventHandleProcessDef = HAPUtilityProcess.getProcessDefinitionElementFromAttachment(eventHandleDef.getProcess().getTaskDefinition(), definition.getAttachmentContainer(), processMan.getPluginManager());
//		HAPExecutableProcess eventHandleProcessExe = HAPProcessorProcess.process(null, new HAPDefinitionProcessWithContext(eventHandleProcessDef), definition.getContext(), allServiceProviders, processMan, contextProcessRequirement, new HAPProcessTracker());
//		HAPExecutableWrapperTask eventHandleProcessExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(pollTaskDef.getProcess(), eventHandleProcessExe, HAPParentContext.createDefault(definition.getContext()), null, contextProcessRequirement);			
//		HAPExecutableEventHandler eventHandler = new HAPExecutableEventHandler(eventHandleDef);
//		eventHandler.setProcess(eventHandleProcessExeWrapper);
//		out.setEventHandler(eventHandler);

		//poll schedule
		
		
		return out;
	}
}
