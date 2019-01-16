package com.nosliw.uiresource.module;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionNode;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.HAPUtilityUIResource;
import com.nosliw.uiresource.page.definition.HAPDefinitionUIEvent;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitResource;

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

		if(parentContext==null)   parentContext = new HAPContextGroup();
		HAPRequirementContextProcessor contextProcessRequirement = HAPUtilityUIResource.getDefaultContextProcessorRequirement(dataTypeHelper, runtime, expressionManager);
		
		//process context 
		out.setContext(HAPProcessorContext.process(moduleDefinition.getContext(), parentContext, null, contextProcessRequirement));
		
		//process global processes in module
		Map<String, HAPDefinitionProcess> internalProcesses = moduleDefinition.getProcesses();
		for(String name : internalProcesses.keySet()) {
			HAPExecutableProcess processExe = HAPProcessorProcess.process(internalProcesses.get(name), name, parentContext, internalProcesses, processMan, contextProcessRequirement, processTracker);
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
		
		//context mapping, only mapping to public context in page
		HAPContextGroup mappingContextGroup = new HAPContextGroup();
		mappingContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, moduleUIDefinition.getContextMapping());
		out.setContextMapping(HAPProcessorContext.process(mappingContextGroup, moduleExe.getContext(), null, contextProcessRequirement).getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC));

		//process page
		String pageId = moduleExe.getDefinition().getPageInfo(moduleUIDefinition.getPage()).getPageId();
		HAPExecutableUIUnitResource page = uiResourceMan.getUIResource(pageId, id, out.getContextMapping());
		out.setPage(page);

		//event handler
		Map<String, HAPDefinitionModuleUIEventHander> eventHandlerDefs = moduleUIDefinition.getEventHandlers();
		for(String eventName :eventHandlerDefs.keySet()) {
			HAPDefinitionModuleUIEventHander eventHandlerDef = eventHandlerDefs.get(eventName);
			HAPExecutableModuleUIEventHandler eventHandlerExe = new HAPExecutableModuleUIEventHandler(eventHandlerDef);

			HAPContextDefinitionRoot eventRootNode = buildContextRootFromEvent(out.getPage().getEventDefinition(eventName));
			HAPContextGroup eventContext = moduleExe.getContext().cloneContextGroup();
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
