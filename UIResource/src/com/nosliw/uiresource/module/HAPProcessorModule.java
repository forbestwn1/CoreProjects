package com.nosliw.uiresource.module;

import java.util.Map;

import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.common.utils.HAPSystemUtility;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcessDefinition;
import com.nosliw.data.core.process.HAPProcessorProcess;
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
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPProcessorDataAssociation;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.common.HAPDefinitionEventHandler;
import com.nosliw.uiresource.common.HAPExecutableEventHandler;
import com.nosliw.uiresource.common.HAPInfoPage;
import com.nosliw.uiresource.common.HAPUtilityCommon;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;

public class HAPProcessorModule {

	public static HAPExecutableModule process(
			HAPDefinitionModule moduleDefinition,
			String id, 
			HAPContextGroup parentContext, 
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPManagerProcessDefinition processMan,
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
		Map<String, HAPDefinitionWrapperTask<HAPDefinitionProcess>> internalProcesses = moduleDefinition.getProcesses();
		for(String name : internalProcesses.keySet()) {
			HAPExecutableProcess processExe = HAPProcessorProcess.process(internalProcesses.get(name).getTaskDefinition(), name, null, out.getContext(), allServiceProviders, processMan, contextProcessRequirement, processTracker);
			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(internalProcesses.get(name), processExe, HAPParentContext.createDefault(out.getContext()), null, contextProcessRequirement);			
			out.addProcess(name, processExeWrapper);
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
			HAPManagerProcessDefinition processMan,
			HAPUIResourceManager uiResourceMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPExecutableModuleUI out = new HAPExecutableModuleUI(moduleUIDefinition, id);
		
		//process page, use context in module override context in page
		HAPInfoPage pageInfo = moduleExe.getDefinition().getPageInfo(moduleUIDefinition.getPage());
		String pageId = pageInfo.getPageId();
		
		//ui decoration
		//ui decoration from page first
		out.addUIDecoration(pageInfo.getDecoration());
		//ui decoration from module ui
		out.addUIDecoration(moduleUIDefinition.getUIDecoration());
		//ui decoration from module
		out.addUIDecoration(moduleExe.getDefinition().getUIDecoration());
		
		HAPContextGroup mappingContextGroup = new HAPContextGroup();
		HAPExecutableDataAssociation daEx = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(moduleExe.getContext()), moduleUIDefinition.getInputMapping(), HAPParentContext.createDefault(HAPContextStructureEmpty.flatStructure()), null, contextProcessRequirement);
		mappingContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, (HAPContext)daEx.getOutput().getOutputStructure());  //.getAssociation().getSolidContext());  kkkk
		HAPExecutableUIUnitPage page = uiResourceMan.getUIPage(pageId, id, mappingContextGroup, null);
		out.setPage(page);

		HAPInfo daConfigure = HAPProcessorDataAssociation.withModifyStructureFalse(new HAPInfoImpSimple());
		//build input data association
		HAPExecutableDataAssociation inputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(moduleExe.getContext()), moduleUIDefinition.getInputMapping(), HAPParentContext.createDefault(page.getFlatContext().getContext()), daConfigure, contextProcessRequirement);
		out.setInputMapping(inputDataAssocation);
		
		//build output data association
		HAPExecutableDataAssociation outputDataAssocation = HAPProcessorDataAssociation.processDataAssociation(HAPParentContext.createDefault(page.getContext()), moduleUIDefinition.getOutputMapping(), HAPParentContext.createDefault(moduleExe.getContext()), daConfigure, contextProcessRequirement);
		out.setOutputMapping(outputDataAssocation);
		
		//event handler
		Map<String, HAPDefinitionEventHandler> eventHandlerDefs = moduleUIDefinition.getEventHandlers();
		for(String eventName :eventHandlerDefs.keySet()) {
			HAPDefinitionEventHandler eventHandlerDef = eventHandlerDefs.get(eventName);
			HAPExecutableEventHandler eventHandlerExe = new HAPExecutableEventHandler(eventHandlerDef);

			HAPContextDefinitionRoot eventRootNode = buildContextRootFromEvent(out.getPage().getEventDefinition(eventName));
			HAPContextGroup eventContext = moduleExe.getContext().cloneContextGroup();
			eventContext.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC).addElement(HAPSystemUtility.buildNosliwFullName("EVENT"), eventRootNode);
			HAPExecutableProcess eventProcessor = HAPProcessorProcess.process(eventHandlerDef.getProcess().getTaskDefinition(), eventName, null, eventContext, serviceProviders, processMan, contextProcessRequirement, processTracker);
			HAPExecutableWrapperTask processExeWrapper = HAPProcessorDataAssociation.processDataAssociationWithTask(eventHandlerDef.getProcess(), eventProcessor, HAPParentContext.createDefault(moduleExe.getContext()), null, contextProcessRequirement);			
			eventHandlerExe.setProcess(processExeWrapper);
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
