package com.nosliw.uiresource.application;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.event.HAPDefinitionHandlerEvent;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentReference;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.process1.HAPProcessorProcess;
import com.nosliw.data.core.process1.HAPUtilityProcessComponent;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcess;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcessSuite;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPRequirementContextProcessor;
import com.nosliw.data.core.structure.temp.HAPProcessorContext;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.module.HAPDefinitionModule;
import com.nosliw.uiresource.module.HAPExecutableModule;

public class HAPProcessMiniAppEntry {

	public static HAPExecutableAppEntry process(
			HAPDefinitionAppEntry minAppEntryDef,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPRuntimeEnvironment runtimeEnv,
			HAPUIResourceManager uiResourceMan,
			HAPProcessTracker processTracker) {

		HAPDefinitionApp miniAppDef = minAppEntryDef.getAppDefinition();

		HAPExecutableAppEntry out = new HAPExecutableAppEntry(minAppEntryDef);
		
		HAPDefinitionAppElementUI entryDefinition = minAppEntryDef.getEntry();
		
		HAPRequirementContextProcessor contextProcessRequirement = HAPUtilityCommon.getDefaultContextProcessorRequirement(runtimeEnv.getResourceDefinitionManager(), runtimeEnv.getDataTypeHelper(), runtimeEnv.getRuntime(), runtimeEnv.getExpressionManager(), runtimeEnv.getServiceManager().getServiceDefinitionManager());
		HAPConfigureProcessorStructure contextProcessConfg = HAPUtilityApp.getContextProcessConfigurationForApp();
		
		//service providers
		Map<String, HAPDefinitionServiceProvider> entryServiceProviders = HAPUtilityServiceUse.buildServiceProvider(entryDefinition.getAttachmentContainer(), null, contextProcessRequirement.serviceDefinitionManager); 

		//context
		out.setContext((HAPValueStructureDefinitionGroup)HAPProcessorContext.process(entryDefinition.getValueStructureWrapper(), HAPContainerStructure.createDefault(miniAppDef.getValueStructureWrapper()), contextProcessConfg, runtimeEnv));

		//process process suite
		HAPResourceDefinitionProcessSuite processSuite = HAPUtilityProcessComponent.buildProcessSuiteFromComponent(minAppEntryDef, runtimeEnv.getProcessManager().getPluginManager()).cloneProcessSuiteDefinition(); 
		processSuite.setValueContext(out.getContext());   //kkkk
//		processSuite.setContext(HAPProcessorContext.process(processSuite.getContext(), HAPParentContext.createDefault(out.getContext()), contextProcessConfg, contextProcessRequirement));
		out.setProcessSuite(processSuite);

		//data definition
		Map<String, HAPDefinitionAppData> dataDefs = miniAppDef.getApplicationData();
		for(String dataDefName : dataDefs.keySet()) {
			HAPValueStructureDefinitionFlat processedContext = (HAPValueStructureDefinitionFlat)HAPProcessorContext.process(dataDefs.get(dataDefName), HAPContainerStructure.createDefault(miniAppDef.getValueStructureWrapper()), contextProcessConfg, runtimeEnv);
			HAPDefinitionAppData processedAppData = dataDefs.get(dataDefName).cloneAppDataDefinition();
			processedContext.toContext(processedAppData);
			out.addApplicationData(dataDefName, processedAppData);
		}
		
		//prepare extra context
		Map<String, HAPValueStructureDefinition> extraContext = out.getExtraContext();
		
		//module
		for(HAPDefinitionAppModule moduleDef : entryDefinition.getModules()) {
			if(HAPUtilityEntityInfo.isEnabled(moduleDef)) {
				HAPExecutableAppModule module = processModule(moduleDef, out, entryDefinition.getAttachmentContainer(), extraContext, entryServiceProviders, runtimeEnv, uiResourceMan, processTracker);
				out.addUIModule(moduleDef.getName(), module);
			}
		}
		
		return out;
	}
	
	private static HAPExecutableAppModule processModule(
			HAPDefinitionAppModule module,
			HAPExecutableAppEntry entryExe,
			HAPDefinitionEntityContainerAttachment parentAttachment,
			Map<String, HAPValueStructureDefinition> extraContexts,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPRuntimeEnvironment runtimeEnv,
			HAPUIResourceManager uiResourceMan,
			HAPProcessTracker processTracker) {
		HAPExecutableAppModule out = new HAPExecutableAppModule(module);
		
		HAPAttachment moduleAttachment = parentAttachment.getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIMODULE, module.getModule());
		HAPEntityOrReference defOrRef = null;
		if(moduleAttachment.getType().equals(HAPConstantShared.ATTACHMENT_TYPE_REFERENCE)) {
			defOrRef = ((HAPAttachmentReference)moduleAttachment).getReferenceId();

		}
		else if(moduleAttachment.getType().equals(HAPConstantShared.ATTACHMENT_TYPE_ENTITY)){
			
		}
		//module
		HAPExecutableModule moduleExe = uiResourceMan.getEmbededUIModule(defOrRef, parentAttachment, module);
		HAPDefinitionModule moduleDef = moduleExe.getDefinition();
		out.setModule(moduleExe);
		
		HAPContainerStructure parentContext = HAPContainerStructure.createDefault(entryExe.getContext());
		for(String extraName : extraContexts.keySet()) {
			parentContext.addStructure(extraName, extraContexts.get(extraName));
		}
		
		HAPInfo daConfigure = HAPProcessorDataAssociation.withModifyOutputStructureConfigureFalse(new HAPInfoImpSimple());

		//input data association
		Map<String, HAPDefinitionDataAssociation> inputDas = module.getInDataAssociations().getDataAssociations();
		for(String inputDaName : inputDas.keySet()) {
			HAPExecutableDataAssociation inputMapping = HAPProcessorDataAssociation.processDataAssociation(parentContext, inputDas.get(inputDaName), HAPContainerStructure.createDefault(moduleDef.getValueStructureGroup().getFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC)), daConfigure, runtimeEnv);
			out.addInputDataAssociation(inputDaName, inputMapping);
		}
		
		
		//output data association
		Map<String, HAPDefinitionDataAssociation> outputMapping = module.getOutDataAssociations().getDataAssociations();
		for(String outputTargetName : outputMapping.keySet()) {
			HAPExecutableDataAssociation outputMappingByTarget = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(moduleDef.getValueStructureGroup()), outputMapping.get(outputTargetName), parentContext, daConfigure, runtimeEnv);
			out.addOutputDataAssociation(outputTargetName, outputMappingByTarget);
		}
		
		//event handler
		Set<HAPDefinitionHandlerEvent> eventHandlerDefs = module.getEventHandlers();
		for(HAPDefinitionHandlerEvent eventHandlerDef :eventHandlerDefs) {
			String eventName = eventHandlerDef.getName();
			HAPResourceDefinitionProcess processDef = entryExe.getProcessDefinition(eventHandlerDef.getTask().getTaskDefinition());
//			HAPDefinitionProcessSuiteElementEntity processDef = HAPUtilityProcess.getProcessDefinitionElementFromAttachment(eventHandlerDef.getProcess().getTaskDefinition(), moduleExe.getDefinition().getAttachmentContainer(), processMan.getPluginManager());
			HAPExecutableProcess eventProcessor = HAPProcessorProcess.process(processDef, null, serviceProviders, runtimeEnv.getProcessManager(), runtimeEnv, processTracker);
			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(eventHandlerDef.getTask(), eventProcessor, HAPContainerStructure.createDefault(moduleExe.getValueStructureWrapper()), null, runtimeEnv);			
			out.addEventHandler(eventName, processExeWrapper);
		}	
		
		return out;
	}
}
