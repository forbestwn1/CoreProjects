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

	public static void process(HAPDefinitionComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
	
		processServiceUse(definition, executable, runtimeEnv);
		
		processTask(definition, executable, runtimeEnv);

		processEvent(definition, executable, runtimeEnv);

		processCommand(definition, executable, runtimeEnv);
		
	}
	
	public static void processServiceUse(HAPDefinitionComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
		for(String serviceName : definition.getAllServices()) {
			HAPExecutableServiceUse serviceExe = HAPProcessorServiceUse.process(definition.getService(serviceName), definition.getValueStructureWrapper().getValueStructure(), definition.getAttachmentContainer(), runtimeEnv);
			executable.addServiceUse(serviceName, serviceExe);
		}
	}

	public static void processTask(HAPDefinitionComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionTaskSuite taskSuite = definition.getTaskSuite();
		HAPContextProcessor processContext = new HAPContextProcessor(definition, runtimeEnv);
		HAPExecutableTaskSuite taskSuiteExe = HAPProcessorTaskSuite.process(null, taskSuite, processContext, runtimeEnv, new HAPProcessTracker());
		executable.setTaskSuite(taskSuiteExe);
	}
	
	public static void processEvent(HAPDefinitionComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
		for(HAPDefinitionEvent eventDef : definition.getEvents()) {
			HAPExecutableEvent eventExe = HAPProcessEvent.processEventDefinition(eventDef, definition.getValueStructureWrapper().getValueStructure(), runtimeEnv);
			executable.addEvent(eventExe);
		}
	}
	
	public static void processCommand(HAPDefinitionComponent definition, HAPExecutableComponent executable, HAPRuntimeEnvironment runtimeEnv) {
		for(HAPDefinitionCommand command : definition.getCommands()) {
			HAPExecutableCommand commandExe = HAPProcessorCommand.process(command, definition.getValueStructureWrapper().getValueStructure(), runtimeEnv);
			executable.addCommand(commandExe);
		}
	}
	
}
