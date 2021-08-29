package com.nosliw.data.core.component;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.component.command.HAPDefinitionCommand;
import com.nosliw.data.core.component.command.HAPExecutableCommand;
import com.nosliw.data.core.component.command.HAPProcessorCommand;
import com.nosliw.data.core.component.event.HAPDefinitionEvent;
import com.nosliw.data.core.component.event.HAPExecutableEvent;
import com.nosliw.data.core.component.event.HAPProcessEvent;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.data.core.service.use.HAPProcessorServiceUse;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;
import com.nosliw.data.core.task.HAPExecutableTaskSuite;
import com.nosliw.data.core.task.HAPProcessorTaskSuite;

public class HAPProcessorComponent {

	private static void processServiceUse(HAPDefinitionComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
		for(String serviceName : definition.getAllServices()) {
			HAPExecutableServiceUse serviceExe = HAPProcessorServiceUse.process(definition.getService(serviceName), definition.getValueStructureWrapper().getValueStructure(), definition.getAttachmentContainer(), runtimeEnv);
			executable.addServiceUse(serviceName, serviceExe);
		}
	}

	private static void processTask(HAPDefinitionComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionTaskSuite taskSuite = definition.getTaskSuite();
		HAPContextProcessor processContext = new HAPContextProcessor(definition, runtimeEnv);
		HAPExecutableTaskSuite taskSuiteExe = HAPProcessorTaskSuite.process(null, taskSuite, processContext, runtimeEnv, new HAPProcessTracker());
		executable.setTaskSuite(taskSuiteExe);
	}
	
	private static void processEvent(HAPDefinitionComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
		for(HAPDefinitionEvent eventDef : definition.getEvents()) {
			HAPExecutableEvent eventExe = HAPProcessEvent.processEventDefinition(eventDef, definition.getValueStructureWrapper().getValueStructure(), runtimeEnv);
			executable.addEvent(eventExe);
		}
	}
	
	private static void processCommand(HAPDefinitionComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
		for(HAPDefinitionCommand command : definition.getCommands()) {
			HAPExecutableCommand commandExe = HAPProcessorCommand.process(command, definition.getValueStructureWrapper().getValueStructure(), runtimeEnv);
			executable.addCommand(commandExe);
		}
	}
	
	
	
}
