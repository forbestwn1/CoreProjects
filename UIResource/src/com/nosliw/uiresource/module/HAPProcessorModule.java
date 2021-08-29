package com.nosliw.uiresource.module;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.component.HAPDefinitionComponent;
import com.nosliw.data.core.component.HAPExecutableComponent;
import com.nosliw.data.core.component.HAPProcessorEmbededComponent;
import com.nosliw.data.core.component.HAPResultSolveReference;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPProcessorServiceUse;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPElementStructureNode;
import com.nosliw.data.core.structure.HAPRequirementContextProcessor;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;

public class HAPProcessorModule {

	public static HAPExecutableModule process(
			HAPDefinitionModule moduleDefinition, 
			String id, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPUIResourceManager uiResourceMan) {
		
		HAPProcessTracker processTracker = new HAPProcessTracker(); 
		return HAPProcessorModule.process(moduleDefinition, id, runtimeEnv, uiResourceMan, processTracker);
	}
	
	
	private static HAPExecutableModule process(
			HAPDefinitionModule moduleDefinition,
			String id, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPUIResourceManager uiResourceMan,
			HAPProcessTracker processTracker) {

		HAPExecutableModule out = new HAPExecutableModule(moduleDefinition, id);

		HAPRequirementContextProcessor contextProcessRequirement1 = HAPUtilityCommon.getDefaultContextProcessorRequirement(runtimeEnv.getResourceDefinitionManager(), runtimeEnv.getDataTypeHelper(), runtimeEnv.getRuntime(), runtimeEnv.getExpressionManager(), runtimeEnv.getServiceManager().getServiceDefinitionManager());
		HAPConfigureProcessorStructure contextProcessConfg = HAPUtilityConfiguration.getContextProcessConfigurationForModule();

		//service
		HAPProcessorServiceUse.process(definition, globalValueStructure, attachmentContainer, runtimeEnv)
		
		//task
		
		
		//attachment
//		HAPUtilityComponent.processAttachmentInChild(moduleDefinition, attachmentReferenceContext.getComplexEntity());
		
		//process value structure
//		HAPValueStructure processedValueStructure = (HAPValueStructure)HAPProcessorStructure.process(moduleDefinition.getValueStructure(), HAPContainerStructure.createDefault(parentValueStructure), moduleDefinition.getAttachmentContainer(), null, contextProcessConfg, runtimeEnv);
//		out.setValueStructure(processedValueStructure);

//		//process process suite
//		HAPResourceDefinitionProcessSuite processSuite = HAPUtilityProcessComponent.buildProcessSuiteFromComponent(moduleDefinition, runtimeEnv.getProcessManager().getPluginManager()).cloneProcessSuiteDefinition(); 
//		processSuite.setValueContext(out.getContext());   //kkk
//		out.setProcessSuite(processSuite);
//		
//		//process lifecycle action
//		Set<HAPHandlerLifecycle> lifecycleActions = moduleDefinition.getLifecycleAction();
//		for(HAPHandlerLifecycle action : lifecycleActions) {
//			HAPResourceDefinitionProcess processDef = out.getProcessDefinition(action.getTask().getTaskDefinition());
//			HAPExecutableProcess processExe = HAPProcessorProcess.process(processDef, null, allServiceProviders, runtimeEnv.getProcessManager(), runtimeEnv, processTracker);
//			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(action.getTask(), processExe, HAPContainerStructure.createDefault(out.getContext()), null, runtimeEnv);			
//			out.addLifecycle(action.getName(), processExeWrapper);
//		}
		
		HAPContextProcessor processContext = new HAPContextProcessor(moduleDefinition, runtimeEnv);
		
		//process ui
		for(HAPDefinitionModuleUI ui : moduleDefinition.getUIs()) {
			if(HAPUtilityEntityInfo.isEnabled(ui)) {
				HAPExecutableModuleUI uiExe = processModuleUI(ui, ui.getName(), out, processContext, uiResourceMan, processTracker);
				out.addModuleUI(uiExe);
			}
		}
		
		return out;
	}
	
	private static HAPExecutableModuleUI processModuleUI(
			HAPDefinitionModuleUI moduleUIDefinition,
			String id,
			HAPExecutableModule moduleExe,
			HAPContextProcessor processContext,
			HAPUIResourceManager uiResourceMan,
			HAPProcessTracker processTracker) {
		HAPExecutableModuleUI out = new HAPExecutableModuleUI(moduleUIDefinition, id);
		
		HAPRuntimeEnvironment runtimeEnv = processContext.getRuntimeEnvironment();
		
		HAPDefinitionModule moduleDef = moduleExe.getDefinition();
		
		
		//resolve page reference
		HAPResultSolveReference refSolveResult = HAPUtilityComponent.solveReference(moduleUIDefinition.getPage(), HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE, processContext);
		HAPExecutableUIUnitPage pageExe = uiResourceMan.getUIPage((HAPDefinitionUIPage)refSolveResult.getEntity(), id, processContext);
		out.setPage(pageExe);

		HAPProcessorEmbededComponent.process(moduleUIDefinition, out, pageExe, moduleExe, runtimeEnv);
		

		
		//input data mapping between module and page
		for(HAPDefinitionDataAssociation dataAssociation : moduleUIDefinition.getInDataAssociations().getDataAssociations()) {
			HAPExecutableDataAssociation inputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(
					HAPContainerStructure.createDefault(moduleExe.getValueStructure()), 
					dataAssociation, 
					HAPContainerStructure.createDefault(pageExe.getUIUnitDefinition().getValueStructure()), 
					null, 
					runtimeEnv);
			out.addInDataAssociation(inputDataAssocation);
		}
		
		//output data mapping
		for(HAPDefinitionDataAssociation dataAssociation : moduleUIDefinition.getOutDataAssociations().getDataAssociations()) {
			HAPExecutableDataAssociation outputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(
					HAPContainerStructure.createDefault(pageExe.getUIUnitDefinition().getValueStructure()), 
					dataAssociation,
					HAPContainerStructure.createDefault(moduleExe.getValueStructure()), 
					null, 
					runtimeEnv);
			out.addOutDataAssociation(outputDataAssocation);
		}

		//ui decoration
		//ui decoration from page first
//		out.addUIDecoration(pageInfo.getDecoration());
		//ui decoration from module ui
		out.addUIDecoration(moduleUIDefinition.getUIDecoration());
		//ui decoration from module
		out.addUIDecoration(moduleExe.getDefinition().getUIDecoration());
		
		//event handler
//		Set<HAPHandler> eventHandlerDefs = moduleUIDefinition.getEventHandlers();
//		for(HAPHandler eventHandlerDef : eventHandlerDefs) {
//			String eventName = eventHandlerDef.getName();
//			HAPRootStructure eventRootNode = buildContextRootFromEvent(out.getPage().getBody().getEventDefinition(eventName));
//			HAPValueStructureDefinitionGroup eventContext = new HAPValueStructureDefinitionGroup();
//			eventContext.addRoot(HAPNosliwUtility.buildNosliwFullName("EVENT"), eventRootNode, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
//			HAPResourceDefinitionProcess processDef = moduleExe.getProcessDefinition(eventHandlerDef.getTask().getTaskDefinition());
//			HAPExecutableProcess eventProcessor = HAPProcessorProcess.process(processDef, eventContext, serviceProviders, processMan, runtimeEnv, processTracker);
//			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(eventHandlerDef.getTask(), eventProcessor, HAPContainerStructure.createDefault(moduleExe.getContext()), null, runtimeEnv);			
//			out.addEventHandler(eventName, processExeWrapper);
//		}	
		
/*		
		HAPExecutableDataAssociation daEx = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(moduleExe.getContext()), moduleUIDefinition.getInputMapping().getDefaultDataAssociation(), HAPContainerStructure.createDefault(HAPValueStructureDefinitionEmpty.flatStructure()), null, runtimeEnv);
		mappingContextGroup.setFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, (HAPValueStructureDefinitionFlat)daEx.getOutput().getOutputStructure());  //.getAssociation().getSolidContext());  kkkk

		
		
		//process page, use context in module override context in page
		//find pape resource id from attachment mapping
		
		
		HAPAttachmentReference pageAttachment = (HAPAttachmentReference)moduleDef.getAttachmentContainer().getElement(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE, moduleUIDefinition.getPage());
		HAPResourceId pageResourceId = pageAttachment.getReferenceId();

		//attachment
//		HAPAttachmentContainer mappedParentAttachment = HAPComponentUtility.buildInternalAttachment(pageResourceId, moduleExe.getDefinition().getAttachmentContainer(), moduleUIDefinition);

		//context
		HAPValueStructureDefinitionGroup mappingContextGroup = new HAPValueStructureDefinitionGroup();
		HAPExecutableDataAssociation daEx = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(moduleExe.getContext()), moduleUIDefinition.getInputMapping().getDefaultDataAssociation(), HAPContainerStructure.createDefault(HAPValueStructureDefinitionEmpty.flatStructure()), null, runtimeEnv);
		mappingContextGroup.setFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, (HAPValueStructureDefinitionFlat)daEx.getOutput().getOutputStructure());  //.getAssociation().getSolidContext());  kkkk

		HAPExecutableUIUnitPage page = uiResourceMan.getEmbededUIPage(pageResourceId, id, mappingContextGroup, null, moduleExe.getDefinition().getAttachmentContainer(), moduleUIDefinition);
		out.setPage(page);

		HAPInfo daConfigure = HAPProcessorDataAssociation.withModifyOutputStructureConfigureFalse(new HAPInfoImpSimple());
		//build input data association
		HAPExecutableDataAssociation inputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(moduleExe.getContext()), moduleUIDefinition.getInputMapping().getDefaultDataAssociation(), HAPContainerStructure.createDefault(page.getBody().getValueStructureExe().getValueStructureDefinitionWrapper()), daConfigure, runtimeEnv);
		out.setInputMapping(inputDataAssocation);
		
		//build output data association
		HAPExecutableDataAssociation outputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(page.getBody().getValueStructureDefinitionWrapper()), moduleUIDefinition.getOutputMapping().getDefaultDataAssociation(), HAPContainerStructure.createDefault(moduleExe.getContext()), daConfigure, runtimeEnv);
		out.setOutputMapping(outputDataAssocation);
		
		//event handler
		Set<HAPHandler> eventHandlerDefs = moduleUIDefinition.getEventHandlers();
		for(HAPHandler eventHandlerDef : eventHandlerDefs) {
			String eventName = eventHandlerDef.getName();
			HAPRootStructure eventRootNode = buildContextRootFromEvent(out.getPage().getBody().getEventDefinition(eventName));
			HAPValueStructureDefinitionGroup eventContext = new HAPValueStructureDefinitionGroup();
			eventContext.addRoot(HAPNosliwUtility.buildNosliwFullName("EVENT"), eventRootNode, HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			HAPResourceDefinitionProcess processDef = moduleExe.getProcessDefinition(eventHandlerDef.getTask().getTaskDefinition());
			HAPExecutableProcess eventProcessor = HAPProcessorProcess.process(processDef, eventContext, serviceProviders, processMan, runtimeEnv, processTracker);
			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(eventHandlerDef.getTask(), eventProcessor, HAPContainerStructure.createDefault(moduleExe.getContext()), null, runtimeEnv);			
			out.addEventHandler(eventName, processExeWrapper);
		}	
*/		
		return out;
	}

	private static HAPRootStructure buildContextRootFromEvent(HAPDefinitionUIEvent eventDef) {
		HAPRootStructure root = new HAPRootStructure();
		HAPElementStructureNode nodeEle = new HAPElementStructureNode();
		
		HAPValueStructureDefinitionFlat dataDef = eventDef.getDataDefinition();
		for(String dataName : dataDef.getRootNames()) {
			nodeEle.addChild(dataName, dataDef.getRoot(dataName).getDefinition());
		}
		root.setDefinition(nodeEle);
		return root;
	}

}
