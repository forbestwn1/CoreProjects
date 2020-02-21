package com.nosliw.uiresource.module;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.common.utils.HAPSystemUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.component.HAPAttachmentReference;
import com.nosliw.data.core.component.HAPHandlerEvent;
import com.nosliw.data.core.component.HAPHandlerLifecycle;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionNode;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextStructureEmpty;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;

public class HAPProcessorModule {

	public static HAPExecutableModule process(
			HAPDefinitionModule moduleDefinition, 
			String id, 
			HAPContextGroup parentContext, 
			HAPManagerProcess processMan,
			HAPUIResourceManager uiResourceMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionManager,
			HAPManagerServiceDefinition serviceDefinitionManager) {
		
		HAPProcessTracker processTracker = new HAPProcessTracker(); 
		return HAPProcessorModule.process(moduleDefinition, id, parentContext, null, processMan, uiResourceMan, dataTypeHelper, runtime, expressionManager, serviceDefinitionManager, processTracker);
	}
	
	private static HAPExecutableModule process(
			HAPDefinitionModule moduleDefinition,
			String id, 
			HAPContextGroup parentContext, 
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPUIResourceManager uiResourceMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionManager,
			HAPManagerServiceDefinition serviceDefinitionManager,
			HAPProcessTracker processTracker) {

		HAPExecutableModule out = new HAPExecutableModule(moduleDefinition, id);

		HAPRequirementContextProcessor contextProcessRequirement = HAPUtilityCommon.getDefaultContextProcessorRequirement(dataTypeHelper, runtime, expressionManager, serviceDefinitionManager);
		HAPConfigureContextProcessor contextProcessConfg = HAPUtilityConfiguration.getContextProcessConfigurationForModule();
		
		//service providers
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(moduleDefinition.getAttachmentContainer(), serviceProviders, contextProcessRequirement.serviceDefinitionManager); 
		
		//process context 
		out.setContextGroup(HAPProcessorContext.process(moduleDefinition.getContext(), HAPParentContext.createDefault(parentContext==null?new HAPContextGroup():parentContext), contextProcessConfg, contextProcessRequirement));

		//process process suite
		HAPDefinitionProcessSuite processSuite = moduleDefinition.getProcessSuite().clone();
		processSuite.setContext(out.getContext());   //kkk
//		processSuite.setContext(HAPProcessorContext.process(processSuite.getContext(), HAPParentContext.createDefault(out.getContext()), contextProcessConfg, contextProcessRequirement));
		out.setProcessSuite(processSuite);
		
		//process lifecycle action
		Set<HAPHandlerLifecycle> lifecycleActions = moduleDefinition.getLifecycleAction();
		for(HAPHandlerLifecycle action : lifecycleActions) {
			HAPDefinitionProcess processDef = out.getProcessDefinition(action.getProcess().getTaskDefinition());
			HAPExecutableProcess processExe = HAPProcessorProcess.process(processDef, null, allServiceProviders, processMan, contextProcessRequirement, processTracker);
			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(action.getProcess(), processExe, HAPParentContext.createDefault(out.getContext()), null, contextProcessRequirement);			
			out.addLifecycle(action.getName(), processExeWrapper);
		}
		
		//process ui
		for(HAPDefinitionModuleUI ui : moduleDefinition.getUIs()) {
			if(!HAPDefinitionModuleUI.STATUS_DISABLED.equals(ui.getStatus())) {
				HAPExecutableModuleUI uiExe = processModuleUI(ui, ui.getName(), out, allServiceProviders, processMan, uiResourceMan, contextProcessRequirement, processTracker);
				out.addModuleUI(uiExe);
			}
		}
		
		return out;
	}
	
	private static HAPExecutableModuleUI processModuleUI(
			HAPDefinitionModuleUI moduleUIDefinition,
			String id,
			HAPExecutableModule moduleExe,
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcess processMan,
			HAPUIResourceManager uiResourceMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPExecutableModuleUI out = new HAPExecutableModuleUI(moduleUIDefinition, id);
		
		//process page, use context in module override context in page
		//find pape resource id from attachment mapping
		HAPAttachmentReference pageAttachment = (HAPAttachmentReference)moduleExe.getDefinition().getAttachmentContainer().getElement(HAPConstant.RUNTIME_RESOURCE_TYPE_UIRESOURCE, moduleUIDefinition.getPage());
		HAPResourceId pageResourceId = pageAttachment.getId();

		//attachment
//		HAPAttachmentContainer mappedParentAttachment = HAPComponentUtility.buildInternalAttachment(pageResourceId, moduleExe.getDefinition().getAttachmentContainer(), moduleUIDefinition);

		//ui decoration
		//ui decoration from page first
//		out.addUIDecoration(pageInfo.getDecoration());
		//ui decoration from module ui
		out.addUIDecoration(moduleUIDefinition.getUIDecoration());
		//ui decoration from module
		out.addUIDecoration(moduleExe.getDefinition().getUIDecoration());
		
		//context
		HAPContextGroup mappingContextGroup = new HAPContextGroup();
		HAPExecutableDataAssociation daEx = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(moduleExe.getContext()), moduleUIDefinition.getInputMapping(), HAPParentContext.createDefault(HAPContextStructureEmpty.flatStructure()), null, contextProcessRequirement);
		mappingContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, (HAPContext)daEx.getOutput().getOutputStructure());  //.getAssociation().getSolidContext());  kkkk

		HAPExecutableUIUnitPage page = uiResourceMan.getEmbededUIPage(pageResourceId, id, mappingContextGroup, null, moduleExe.getDefinition().getAttachmentContainer(), moduleUIDefinition);
		out.setPage(page);

		HAPInfo daConfigure = HAPProcessorDataAssociation.withModifyStructureFalse(new HAPInfoImpSimple());
		//build input data association
		HAPExecutableDataAssociation inputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(moduleExe.getContext()), moduleUIDefinition.getInputMapping(), HAPParentContext.createDefault(page.getFlatContext().getContext()), daConfigure, contextProcessRequirement);
		out.setInputMapping(inputDataAssocation);
		
		//build output data association
		HAPExecutableDataAssociation outputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(page.getContext()), moduleUIDefinition.getOutputMapping(), HAPParentContext.createDefault(moduleExe.getContext()), daConfigure, contextProcessRequirement);
		out.setOutputMapping(outputDataAssocation);
		
		//event handler
		Set<HAPHandlerEvent> eventHandlerDefs = moduleUIDefinition.getEventHandlers();
		for(HAPHandlerEvent eventHandlerDef : eventHandlerDefs) {
			String eventName = eventHandlerDef.getName();
			HAPContextDefinitionRoot eventRootNode = buildContextRootFromEvent(out.getPage().getEventDefinition(eventName));
			HAPContextGroup eventContext = new HAPContextGroup();
			eventContext.addElement(HAPSystemUtility.buildNosliwFullName("EVENT"), eventRootNode, HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC);
			HAPDefinitionProcess processDef = moduleExe.getProcessDefinition(eventHandlerDef.getProcess().getTaskDefinition());
			HAPExecutableProcess eventProcessor = HAPProcessorProcess.process(processDef, eventContext, serviceProviders, processMan, contextProcessRequirement, processTracker);
			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(eventHandlerDef.getProcess(), eventProcessor, HAPParentContext.createDefault(moduleExe.getContext()), null, contextProcessRequirement);			
			out.addEventHandler(eventName, processExeWrapper);
		}	
		return out;
	}

	private static HAPContextDefinitionRoot buildContextRootFromEvent(HAPDefinitionUIEvent eventDef) {
		HAPContextDefinitionRoot root = new HAPContextDefinitionRoot();
		HAPContextDefinitionNode nodeEle = new HAPContextDefinitionNode();
		
		HAPContext dataDef = eventDef.getDataDefinition();
		for(String dataName : dataDef.getElementNames()) {
			nodeEle.addChild(dataName, dataDef.getElement(dataName).getDefinition());
		}
		root.setDefinition(nodeEle);
		return root;
	}

}
