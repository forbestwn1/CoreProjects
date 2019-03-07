package com.nosliw.uiresource.module;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPDefinitionEmbededProcess;
import com.nosliw.data.core.process.HAPExecutableEmbededProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionNode;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociationWithTarget;
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
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = HAPUtilityServiceUse.buildServiceProvider(serviceProviders, moduleDefinition, contextProcessRequirement.serviceDefinitionManager); 
		
		//process context 
		out.setContextGroup(HAPProcessorContext.process(moduleDefinition.getContext(), HAPParentContext.createDefault(parentContext==null?new HAPContextGroup():parentContext), contextProcessConfg, contextProcessRequirement));

		//process global processes in module
		Map<String, HAPDefinitionEmbededProcess> internalProcesses = moduleDefinition.getProcesses();
		for(String name : internalProcesses.keySet()) {
			HAPExecutableEmbededProcess processExe = HAPProcessorProcess.process(internalProcesses.get(name), name, out.getContext(), null, allServiceProviders, processMan, contextProcessRequirement, processTracker);
			out.addProcess(name, processExe);
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
		String pageId = moduleExe.getDefinition().getPageInfo(moduleUIDefinition.getPage()).getPageId();

		HAPContextGroup mappingContextGroup = new HAPContextGroup();
		mappingContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(moduleExe.getContext()), moduleUIDefinition.getInputMapping(), true, contextProcessRequirement).getSolidContext());
		HAPExecutableUIUnitPage page = uiResourceMan.getUIPage(pageId, id, mappingContextGroup, null);
		out.setPage(page);

		//build input data association
		HAPExecutableDataAssociationWithTarget inputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(moduleExe.getContext()), moduleUIDefinition.getInputMapping(), page.getFlatContext().getContext(), false, contextProcessRequirement);
		out.setInputMapping(inputDataAssocation);
		
		//build output data association
		HAPExecutableDataAssociationWithTarget outputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(page.getContext()), moduleUIDefinition.getOutputMapping(), moduleExe.getContext(), false, contextProcessRequirement);
		out.setOutputMapping(outputDataAssocation);
		
		//event handler
		Map<String, HAPDefinitionModuleUIEventHander> eventHandlerDefs = moduleUIDefinition.getEventHandlers();
		for(String eventName :eventHandlerDefs.keySet()) {
			HAPDefinitionModuleUIEventHander eventHandlerDef = eventHandlerDefs.get(eventName);
			HAPExecutableModuleUIEventHandler eventHandlerExe = new HAPExecutableModuleUIEventHandler(eventHandlerDef);

			HAPContextDefinitionRoot eventRootNode = buildContextRootFromEvent(out.getPage().getEventDefinition(eventName));
			HAPContextGroup eventContext = moduleExe.getContext().cloneContextGroup();
			eventContext.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC).addElement("EVENT", eventRootNode);
			eventHandlerExe.setProcess(HAPProcessorProcess.process(eventHandlerDef.getProcess(), eventName, eventContext, null, serviceProviders, processMan, contextProcessRequirement, processTracker));
			out.addEventHandler(eventName, eventHandlerExe);
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
