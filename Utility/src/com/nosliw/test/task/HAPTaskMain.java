package com.nosliw.test.task;

import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;
import com.nosliw.data.core.task.HAPImporterTaskSuiteDefinition;

public class HAPTaskMain {

	public static void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		HAPDefinitionTaskSuite suite = HAPImporterTaskSuiteDefinition.readTaskSuiteDefinitionFromFile(HAPTaskMain.class.getResourceAsStream("expression.task"), runtimeEnvironment.getTaskManager());
		
		runtimeEnvironment.getTaskManager().executeTask("main", suite);
		
	}

}
