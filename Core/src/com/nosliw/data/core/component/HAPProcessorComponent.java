package com.nosliw.data.core.component;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.command.HAPDefinitionCommand;
import com.nosliw.data.core.component.command.HAPExecutableCommand;
import com.nosliw.data.core.component.command.HAPProcessorCommand;
import com.nosliw.data.core.component.event.HAPDefinitionEvent;
import com.nosliw.data.core.component.event.HAPExecutableEvent;
import com.nosliw.data.core.component.event.HAPProcessEvent;
import com.nosliw.data.core.domain.entity.valuestructure.HAPProcessorValueStructureInComponent;
import com.nosliw.data.core.domain.entity.valuestructure.HAPValueStructureInComplex;
import com.nosliw.data.core.domain.entity.valuestructure.HAPValueStructureGrouped;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.data.core.service.use.HAPProcessorServiceUse;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;
import com.nosliw.data.core.task.HAPExecutableTaskSuite;
import com.nosliw.data.core.task.HAPProcessorTaskSuite;

public class HAPProcessorComponent {

	//normalize definition
	public static HAPDefinitionEntityComponent normalize(HAPDefinitionEntityComponent definition, HAPContextProcessor processContext) {
		
		HAPValueStructureGrouped valueStructureWrapper = definition.getValueStructureWrapper();
		
		//expand reference in value structure
		valueStructureWrapper.setValueStructure(HAPProcessorValueStructureInComponent.expandReference((HAPValueStructureInComplex)valueStructureWrapper.getValueStructure(), processContext));

		//
		normalizeService(definition, processContext.getRuntimeEnvironment());
		
		//enhance value structure according to mapping with service
		for(String serviceName : definition.getAllServices()) {
			HAPDefinitionServiceUse service = definition.getService(serviceName);
			HAPProcessorServiceUse.enhanceValueStructureByService(service, valueStructureWrapper.getValueStructure(), processContext.getRuntimeEnvironment());
		}

		//enhance value structure according to mapping with command
		
		//enhance value structure according to event definition
		
		return definition;
	}
	
	//process definition to executable
	public static void process(HAPDefinitionEntityComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
	
		processServiceUse(definition, executable, runtimeEnv);
		
		processTask(definition, executable, runtimeEnv);

		processEvent(definition, executable, runtimeEnv);

		processCommand(definition, executable, runtimeEnv);
		
	}
	
	public static void normalizeService(HAPDefinitionEntityComponent definition, HAPRuntimeEnvironment runtimeEnv) {
		for(String serviceName : definition.getAllServices()) {
			HAPDefinitionServiceUse service = definition.getService(serviceName);
			HAPProcessorServiceUse.normalizeServiceUse(service, definition.getAttachmentContainer(), runtimeEnv);
		}
	}

	
	public static void processServiceUse(HAPDefinitionEntityComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
		for(String serviceName : definition.getAllServices()) {
			HAPExecutableServiceUse serviceExe = HAPProcessorServiceUse.process(definition.getService(serviceName), definition.getValueStructureWrapper().getValueStructure(), definition.getAttachmentContainer(), runtimeEnv);
			executable.addServiceUse(serviceName, serviceExe);
		}
	}

	public static void processTask(HAPDefinitionEntityComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionTaskSuite taskSuite = definition.getTaskSuite();
		HAPContextProcessor processContext = new HAPContextProcessor(definition, runtimeEnv);
		HAPExecutableTaskSuite taskSuiteExe = HAPProcessorTaskSuite.process(null, taskSuite, processContext, runtimeEnv, new HAPProcessTracker());
		executable.setTaskSuite(taskSuiteExe);
	}
	
	public static void processEvent(HAPDefinitionEntityComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
		for(HAPDefinitionEvent eventDef : definition.getEvents()) {
			HAPExecutableEvent eventExe = HAPProcessEvent.processEventDefinition(eventDef, definition.getValueStructureWrapper().getValueStructure(), runtimeEnv);
			executable.addEvent(eventExe);
		}
	}
	
	public static void processCommand(HAPDefinitionEntityComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
		for(HAPDefinitionCommand command : definition.getCommands()) {
			HAPExecutableCommand commandExe = HAPProcessorCommand.process(command, definition.getValueStructureWrapper().getValueStructure(), runtimeEnv);
			executable.addCommand(commandExe);
		}
	}
	
}
