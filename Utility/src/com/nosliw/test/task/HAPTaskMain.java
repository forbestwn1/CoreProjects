package com.nosliw.test.task;

import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.util.HAPImporterProcessSuiteDefinition;

public class HAPTaskMain {

	public static void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		HAPDefinitionProcessSuite suite = HAPImporterProcessSuiteDefinition.readProcessSuiteDefinitionFromFile(HAPTaskMain.class.getResourceAsStream("expression.task"), runtimeEnvironment.getProcessManager());
		
//		runtimeEnvironment.getTaskManager().executeTask("main", suite);
		
	}

}
