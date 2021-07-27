package com.nosliw.uiresource.module;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPNosliwUtility;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.HAPHandlerLifecycle;
import com.nosliw.data.core.component.HAPResultSolveReference;
import com.nosliw.data.core.component.HAPUtilityComponent;
import com.nosliw.data.core.component.attachment.HAPContextProcessAttachmentReference;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.dataassociation.HAPUtilityDataAssociation;
import com.nosliw.data.core.handler.HAPHandler;
import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.process1.HAPProcessorProcess;
import com.nosliw.data.core.process1.HAPUtilityProcessComponent;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcess;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcessSuite;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPElementStructureNode;
import com.nosliw.data.core.structure.HAPRequirementContextProcessor;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.structure.temp.HAPProcessorContext;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionEmpty;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIPage;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;

public class HAPProcessorModule {

	public static HAPExecutableModule process(
			HAPDefinitionModule moduleDefinition, 
			String id, 
			HAPValueStructureDefinitionGroup parentContext, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPUIResourceManager uiResourceMan) {
		
		HAPProcessTracker processTracker = new HAPProcessTracker(); 
		return HAPProcessorModule.process(moduleDefinition, id, parentContext, runtimeEnv, uiResourceMan, processTracker);
	}
	
	private static HAPExecutableModule process(
			HAPDefinitionModule moduleDefinition,
			String id, 
			HAPValueStructureDefinitionGroup parentContext, 
			HAPRuntimeEnvironment runtimeEnv,
			HAPUIResourceManager uiResourceMan,
			HAPProcessTracker processTracker) {

		HAPExecutableModule out = new HAPExecutableModule(moduleDefinition, id);

		HAPRequirementContextProcessor contextProcessRequirement1 = HAPUtilityCommon.getDefaultContextProcessorRequirement(runtimeEnv.getResourceDefinitionManager(), runtimeEnv.getDataTypeHelper(), runtimeEnv.getRuntime(), runtimeEnv.getExpressionManager(), runtimeEnv.getServiceManager().getServiceDefinitionManager());
		HAPConfigureProcessorStructure contextProcessConfg = HAPUtilityConfiguration.getContextProcessConfigurationForModule();
		
		//service providers
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(moduleDefinition.getAttachmentContainer(), runtimeEnv.getServiceManager().getServiceDefinitionManager()); 
		
		//process context 
		out.setContextGroup((HAPValueStructureDefinitionGroup)HAPProcessorContext.process(moduleDefinition.getValueStructureWrapper(), HAPContainerStructure.createDefault(parentContext==null?new HAPValueStructureDefinitionGroup():parentContext), contextProcessConfg, runtimeEnv));

		//process process suite
		HAPResourceDefinitionProcessSuite processSuite = HAPUtilityProcessComponent.buildProcessSuiteFromComponent(moduleDefinition, runtimeEnv.getProcessManager().getPluginManager()).cloneProcessSuiteDefinition(); 
		processSuite.setValueContext(out.getContext());   //kkk
		out.setProcessSuite(processSuite);
		
		//process lifecycle action
		Set<HAPHandlerLifecycle> lifecycleActions = moduleDefinition.getLifecycleAction();
		for(HAPHandlerLifecycle action : lifecycleActions) {
			HAPResourceDefinitionProcess processDef = out.getProcessDefinition(action.getTask().getTaskDefinition());
			HAPExecutableProcess processExe = HAPProcessorProcess.process(processDef, null, allServiceProviders, runtimeEnv.getProcessManager(), runtimeEnv, processTracker);
			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(action.getTask(), processExe, HAPContainerStructure.createDefault(out.getContext()), null, runtimeEnv);			
			out.addLifecycle(action.getName(), processExeWrapper);
		}
		
		//process ui
		for(HAPDefinitionModuleUI ui : moduleDefinition.getUIs()) {
			if(!HAPUtilityEntityInfo.isEnabled(ui)) {
				HAPExecutableModuleUI uiExe = processModuleUI(ui, ui.getName(), out, runtimeEnv, processTracker);
				out.addModuleUI(uiExe);
			}
		}
		
		return out;
	}
	
	private static HAPExecutableModuleUI processModuleUI(
			HAPDefinitionModuleUI moduleUIDefinition,
			String id,
			HAPExecutableModule moduleExe,
			HAPContextProcessAttachmentReference attachmentReferenceContext,
			HAPUIResourceManager uiResourceMan,
			HAPProcessTracker processTracker) {
		HAPExecutableModuleUI out = new HAPExecutableModuleUI(moduleUIDefinition, id);
		
		HAPRuntimeEnvironment runtimeEnv = attachmentReferenceContext.getRuntimeEnvironment();
		
		HAPDefinitionModule moduleDef = moduleExe.getDefinition();
		
		//value structure
		HAPValueStructureDefinitionGroup mappingContextGroup = new HAPValueStructureDefinitionGroup();
		HAPExecutableDataAssociation daEx = HAPProcessorDataAssociation.processDataAssociation(HAPContainerStructure.createDefault(moduleExe.getContext()), moduleUIDefinition.getInputMapping().getDefaultDataAssociation(), HAPContainerStructure.createDefault(HAPValueStructureDefinitionEmpty.flatStructure()), null, runtimeEnv);
		mappingContextGroup.setFlat(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC, (HAPValueStructureDefinitionFlat)daEx.getOutput().getOutputStructure());  //.getAssociation().getSolidContext());  kkkk

		
		//resolve page reference
		HAPResultSolveReference refSolveResult = HAPUtilityComponent.solveReference(moduleUIDefinition.getPage(), HAPConstantShared.RUNTIME_RESOURCE_TYPE_UIRESOURCE, attachmentReferenceContext);
		HAPExecutableUIUnitPage pageExe = uiResourceMan.getUIPage((HAPDefinitionUIPage)refSolveResult.getEntity(), id, moduleDef.getValueStructure(), attachmentReferenceContext);

		//input data mapping between module and page
		HAPExecutableDataAssociation inputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(
				HAPContainerStructure.createDefault(moduleExe.getValueStructureWrapper().getValueStructure()), 
				moduleUIDefinition.getInputMapping().getDefaultDataAssociation(), 
				HAPContainerStructure.createDefault(pageExe.getUIUnitDefinition().getValueStructure()), 
				attachmentReferenceContext.getComplexEntity().getAttachmentContainer(), 
				null, 
				runtimeEnv);
		out.setInputMapping(inputDataAssocation);

		HAPExecutableDataAssociation outputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(
				HAPContainerStructure.createDefault(pageExe.getUIUnitDefinition().getValueStructure()), 
				HAPUtilityDataAssociation.reverseMapping(moduleUIDefinition.getInputMapping().getDefaultDataAssociation()), 
				HAPContainerStructure.createDefault(moduleExe.getValueStructureWrapper().getValueStructure()), 
				attachmentReferenceContext.getComplexEntity().getAttachmentContainer(), 
				null, 
				runtimeEnv);
		out.setOutputMapping(outputDataAssocation);

		
		//ui decoration
		//ui decoration from page first
//		out.addUIDecoration(pageInfo.getDecoration());
		//ui decoration from module ui
		out.addUIDecoration(moduleUIDefinition.getUIDecoration());
		//ui decoration from module
		out.addUIDecoration(moduleExe.getDefinition().getUIDecoration());
		
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
