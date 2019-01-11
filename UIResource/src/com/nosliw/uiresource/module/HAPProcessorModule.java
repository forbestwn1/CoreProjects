package com.nosliw.uiresource.module;

import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;

public class HAPProcessorModule {

	public HAPExecutableModule process(
			HAPDefinitionModule moduleDefinition,
			String id, 
			HAPContextGroup parentContext, 
			HAPManagerProcess processMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		
		HAPExecutableModule out = new HAPExecutableModule(moduleDefinition, id);
		
		//process context 
		out.setContext(HAPProcessorContext.process(moduleDefinition.getContext(), parentContext, null, contextProcessRequirement));
		
		//process process
		Map<String, HAPDefinitionProcess> internalProcesses = moduleDefinition.getProcesses();
		for(String name : internalProcesses.keySet()) {
			HAPExecutableProcess processExe = HAPProcessorProcess.process(internalProcesses.get(name), name, parentContext, internalProcesses, processMan, contextProcessRequirement, processTracker);
			out.addProcess(name, processExe);
		}

		//process ui
		for(HAPDefinitionModuleUI ui : moduleDefinition.getUIs()) {
			HAPExecutableModuleUI uiExe = process(ui, ui.getName(), out, processMan, contextProcessRequirement, processTracker);
			out.addModuleUI(uiExe);
		}
		
		return out;
	}
	
	private HAPExecutableModuleUI process(
			HAPDefinitionModuleUI moduleUIDefinition,
			String id,
			HAPExecutableModule moduleExe,
			HAPManagerProcess processMan,
			HAPRequirementContextProcessor contextProcessRequirement,
			HAPProcessTracker processTracker) {
		HAPExecutableModuleUI out = new HAPExecutableModuleUI(moduleUIDefinition, id);
		
		//context mapping
		HAPContextGroup mappingContextGroup = new HAPContextGroup();
		mappingContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, moduleUIDefinition.getContextMapping());
		out.setContextMapping(HAPProcessorContext.process(mappingContextGroup, moduleExe.getContext(), null, contextProcessRequirement).getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC));
		
		//event handler
		Map<String, HAPDefinitionModuleUIEventHander> eventHandlerDefs = moduleUIDefinition.getEventHandlers();
		for(String eventName :eventHandlerDefs.keySet()) {
			HAPDefinitionModuleUIEventHander eventHandlerDef = eventHandlerDefs.get(eventName);
			HAPExecutableModuleUIEventHandler eventHandlerExe = new HAPExecutableModuleUIEventHandler(eventHandlerDef);
			eventHandlerExe.setProcess(HAPProcessorProcess.process(eventHandlerDef.getProcess(), eventName, moduleExe.getContext(), null, processMan, contextProcessRequirement, processTracker));
		}
		
		return out;
	}
	
	
}
