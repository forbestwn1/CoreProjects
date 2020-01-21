package com.nosliw.test.event;

import com.nosliw.data.core.event.HAPDefinitionEventTask;
import com.nosliw.data.core.event.HAPExecutableEventTask;
import com.nosliw.data.core.event.HAPParserEventTask;
import com.nosliw.data.core.event.HAPProcessorEventTask;
import com.nosliw.data.core.event.HAPUtilityEventTask;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;

public class HAPEventMain {

	public static void main(String[] args) {
		
		HAPDefinitionEventTask eventTaskDef = HAPUtilityEventTask.getEventTaskDefinitionById("flightarrive", HAPParserEventTask.getInstance());
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();

		HAPExecutableEventTask eventTaskExe = HAPProcessorEventTask.process(eventTaskDef, runtimeEnvironment.getProcessDefinitionManager(), HAPExpressionManager.dataTypeHelper, runtimeEnvironment.getRuntime(), runtimeEnvironment.getExpressionSuiteManager(), runtimeEnvironment.getServiceManager().getServiceDefinitionManager());
		
		
		
	}
	
	
}
