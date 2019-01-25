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
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionNode;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.HAPUtilityUIResource;
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
			HAPProcessTracker processTracker) {

		HAPExecutableModule out = new HAPExecutableModule(moduleDefinition, id);

		HAPRequirementContextProcessor contextProcessRequirement = HAPUtilityUIResource.getDefaultContextProcessorRequirement(dataTypeHelper, runtime, expressionManager);
		
		//process context 
		out.setContextGroup(HAPProcessorContext.process(moduleDefinition.getContext(), parentContext==null?new HAPContextGroup():parentContext, null, contextProcessRequirement));

		//process global processes in module
		Map<String, HAPDefinitionEmbededProcess> internalProcesses = moduleDefinition.getProcesses();
		for(String name : internalProcesses.keySet()) {
			HAPExecutableEmbededProcess processExe = HAPProcessorProcess.process(internalProcesses.get(name), name, out.getContextGroup(), null, processMan, contextProcessRequirement, processTracker);
			out.addProcess(name, processExe);
		}

		//process ui
		for(HAPDefinitionModuleUI ui : moduleDefinition.getUIs()) {
			HAPExecutableModuleUI uiExe = process(ui, ui.getName(), out, processMan, uiResourceMan, contextProcessRequirement, processTracker);
			out.addModuleUI(uiExe);
		}
		
		return out;
	}
	
	private static HAPExecutableModuleUI process(
			HAPDefinitionModuleUI moduleUIDefinition,
			String id,
			HAPExecutableModule moduleExe,
			HAPManagerProcess processMan,
			HAPUIResourceManager uiResourceMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPExecutableModuleUI out = new HAPExecutableModuleUI(moduleUIDefinition, id);
		
		//process page
		String pageId = moduleExe.getDefinition().getPageInfo(moduleUIDefinition.getPage()).getPageId();
		HAPContextGroup mappingContextGroup = new HAPContextGroup();
		mappingContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, moduleUIDefinition.getContextMapping());
		HAPExecutableUIUnitPage page = uiResourceMan.getUIResource(pageId, id, mappingContextGroup, moduleExe.getContextGroup());
		out.setContextMapping(page.getContext());
//		out.setContextMapping(HAPProcessorContext.process(mappingContextGroup, moduleExe.getContextGroup(), null, contextProcessRequirement).getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC));
		out.setPage(page);

		//event handler
		Map<String, HAPDefinitionModuleUIEventHander> eventHandlerDefs = moduleUIDefinition.getEventHandlers();
		for(String eventName :eventHandlerDefs.keySet()) {
			HAPDefinitionModuleUIEventHander eventHandlerDef = eventHandlerDefs.get(eventName);
			HAPExecutableModuleUIEventHandler eventHandlerExe = new HAPExecutableModuleUIEventHandler(eventHandlerDef);

			HAPContextDefinitionRoot eventRootNode = buildContextRootFromEvent(out.getPage().getEventDefinition(eventName));
			HAPContextGroup eventContext = moduleExe.getContextGroup().cloneContextGroup();
			eventContext.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC).addElement("EVENT", eventRootNode);
			eventHandlerExe.setProcess(HAPProcessorProcess.process(eventHandlerDef.getProcess(), eventName, eventContext, null, processMan, contextProcessRequirement, processTracker));
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
