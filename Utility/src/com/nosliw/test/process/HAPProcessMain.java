package com.nosliw.test.process;

import java.io.FileNotFoundException;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;

public class HAPProcessMain {

	static public void main(String[] args) throws FileNotFoundException {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		HAPDefinitionProcessSuite suite = HAPUtilityProcess.getProcessSuite("expression", runtimeEnvironment.getProcessManager().getPluginManager()); 
		
		String id = "expressionProcessId";
		HAPExecutableProcess processed = HAPProcessorProcess.process(id, suite, new HAPProcessContext(), runtimeEnvironment.getProcessManager(), new HAPEnvContextProcessor(HAPExpressionManager.dataTypeHelper, runtimeEnvironment.getRuntime(), runtimeEnvironment.getExpressionSuiteManager(), null));
		System.out.println(processed.toResourceData(null));
	}
	
}
