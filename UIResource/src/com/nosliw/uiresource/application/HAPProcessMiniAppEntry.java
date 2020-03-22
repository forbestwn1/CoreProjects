package com.nosliw.uiresource.application;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.common.HAPEntityOrReference;
import com.nosliw.data.core.component.HAPHandlerEvent;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.module.HAPDefinitionModule;
import com.nosliw.uiresource.module.HAPDefinitionModuleUI;
import com.nosliw.uiresource.module.HAPExecutableModule;

public class HAPProcessMiniAppEntry {

	public static HAPExecutableAppEntry process(
			HAPDefinitionAppEntry minAppEntryDef,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerResourceDefinition resourceDefMan,
			HAPManagerProcess processMan,
			HAPUIResourceManager uiResourceMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPRuntime runtime, 
			HAPManagerExpression expressionManager,
			HAPManagerServiceDefinition serviceDefinitionManager,
			HAPProcessTracker processTracker) {

		HAPDefinitionApp miniAppDef = minAppEntryDef.getAppDefinition();

		HAPExecutableAppEntry out = new HAPExecutableAppEntry(minAppEntryDef);
		
		HAPDefinitionAppElementUI entryDefinition = minAppEntryDef.getEntry();
		
		HAPRequirementContextProcessor contextProcessRequirement = HAPUtilityCommon.getDefaultContextProcessorRequirement(resourceDefMan, dataTypeHelper, runtime, expressionManager, serviceDefinitionManager);
		HAPConfigureContextProcessor contextProcessConfg = HAPUtilityApp.getContextProcessConfigurationForApp();
		
		//service providers
		Map<String, HAPDefinitionServiceProvider> entryServiceProviders = HAPUtilityServiceUse.buildServiceProvider(entryDefinition.getAttachmentContainer(), null, contextProcessRequirement.serviceDefinitionManager); 

		//context
		out.setContext((HAPContextGroup)HAPProcessorContext.process(entryDefinition.getContextStructure(), HAPParentContext.createDefault(miniAppDef.getContextStructure()), contextProcessConfg, contextProcessRequirement));

		//process process suite
		HAPDefinitionProcessSuite processSuite = HAPUtilityComponent.getProcessSuite(minAppEntryDef, processMan.getPluginManager()).cloneProcessSuiteDefinition(); 
		processSuite.setContextStructure(out.getContext());   //kkkk
//		processSuite.setContext(HAPProcessorContext.process(processSuite.getContext(), HAPParentContext.createDefault(out.getContext()), contextProcessConfg, contextProcessRequirement));
		out.setProcessSuite(processSuite);

		//data definition
		Map<String, HAPDefinitionAppData> dataDefs = miniAppDef.getApplicationData();
		for(String dataDefName : dataDefs.keySet()) {
			HAPContext processedContext = (HAPContext)HAPProcessorContext.process(dataDefs.get(dataDefName), HAPParentContext.createDefault(miniAppDef.getContextStructure()), contextProcessConfg, contextProcessRequirement);
			HAPDefinitionAppData processedAppData = dataDefs.get(dataDefName).cloneAppDataDefinition();
			processedContext.toContext(processedAppData);
			out.addApplicationData(dataDefName, processedAppData);
		}
		
		//prepare extra context
		Map<String, HAPContextStructure> extraContext = out.getExtraContext();
		
		//module
		for(HAPDefinitionAppModule moduleDef : entryDefinition.getModules()) {
			if(!HAPDefinitionModuleUI.STATUS_DISABLED.equals(moduleDef.getStatus())) {
				HAPExecutableAppModule module = processModule(moduleDef, out, entryDefinition.getAttachmentContainer(), extraContext, entryServiceProviders, processMan, uiResourceMan, contextProcessRequirement, processTracker);
				out.addUIModule(moduleDef.getName(), module);
			}
		}
		
		return out;
	}
	
	private static HAPExecutableAppModule processModule(
			HAPDefinitionAppModule module,
			HAPExecutableAppEntry entryExe,
			HAPAttachmentContainer parentAttachment,
			Map<String, HAPContextStructure> extraContexts,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPUIResourceManager uiResourceMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPExecutableAppModule out = new HAPExecutableAppModule(module);
		
		HAPAttachment moduleAttachment = parentAttachment.getElement(HAPConstant.RUNTIME_RESOURCE_TYPE_UIMODULE, module.getModule());
		HAPEntityOrReference defOrRef = null;
		if(moduleAttachment.getType().equals(HAPConstant.ATTACHMENT_TYPE_REFERENCE)) {
			defOrRef = ((HAPAttachmentReference)moduleAttachment).getId();

		}
		else if(moduleAttachment.getType().equals(HAPConstant.ATTACHMENT_TYPE_ENTITY)){
			
		}
		//module
		HAPExecutableModule moduleExe = uiResourceMan.getEmbededUIModule(defOrRef, parentAttachment, module);
		HAPDefinitionModule moduleDef = moduleExe.getDefinition();
		out.setModule(moduleExe);
		
		HAPParentContext parentContext = HAPParentContext.createDefault(entryExe.getContext());
		for(String extraName : extraContexts.keySet()) {
			parentContext.addContext(extraName, extraContexts.get(extraName));
		}
		
		HAPInfo daConfigure = HAPProcessorDataAssociation.withModifyStructureFalse(new HAPInfoImpSimple());

		//input data association
		Map<String, HAPDefinitionDataAssociation> inputDas = module.getInputMapping().getDataAssociations();
		for(String inputDaName : inputDas.keySet()) {
			HAPExecutableDataAssociation inputMapping = HAPProcessorDataAssociation.processDataAssociation(parentContext, inputDas.get(inputDaName), HAPParentContext.createDefault(moduleDef.getContextNotFlat().getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC)), daConfigure, contextProcessRequirement);
			out.addInputDataAssociation(inputDaName, inputMapping);
		}
		
		
		//output data association
		Map<String, HAPDefinitionDataAssociation> outputMapping = module.getOutputMapping().getDataAssociations();
		for(String outputTargetName : outputMapping.keySet()) {
			HAPExecutableDataAssociation outputMappingByTarget = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(moduleDef.getContextNotFlat()), outputMapping.get(outputTargetName), parentContext, daConfigure, contextProcessRequirement);
			out.addOutputDataAssociation(outputTargetName, outputMappingByTarget);
		}
		
		//event handler
		Set<HAPHandlerEvent> eventHandlerDefs = module.getEventHandlers();
		for(HAPHandlerEvent eventHandlerDef :eventHandlerDefs) {
			String eventName = eventHandlerDef.getName();
			HAPDefinitionProcess processDef = entryExe.getProcessDefinition(eventHandlerDef.getTask().getTaskDefinition());
//			HAPDefinitionProcessSuiteElementEntity processDef = HAPUtilityProcess.getProcessDefinitionElementFromAttachment(eventHandlerDef.getProcess().getTaskDefinition(), moduleExe.getDefinition().getAttachmentContainer(), processMan.getPluginManager());
			HAPExecutableProcess eventProcessor = HAPProcessorProcess.process(processDef, null, serviceProviders, processMan, contextProcessRequirement, processTracker);
			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(eventHandlerDef.getTask(), eventProcessor, HAPParentContext.createDefault(moduleExe.getContext()), null, contextProcessRequirement);			
			out.addEventHandler(eventName, processExeWrapper);
		}	
		
		return out;
	}
}
