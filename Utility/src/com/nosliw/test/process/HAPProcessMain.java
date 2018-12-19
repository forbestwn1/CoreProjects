package com.nosliw.test.process;

import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.util.HAPImporterProcessSuiteDefinition;
import com.nosliw.test.task.HAPTaskMain;

public class HAPProcessMain {

	static public void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		HAPDefinitionProcessSuite suite = HAPImporterProcessSuiteDefinition.readProcessSuiteDefinitionFromFile(HAPProcessMain.class.getResourceAsStream("expression.process"), runtimeEnvironment.getProcessManager());
		
		
		
	}
	
}
