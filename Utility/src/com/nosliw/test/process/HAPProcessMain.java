package com.nosliw.test.process;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.process.util.HAPImporterProcessSuiteDefinition;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;

public class HAPProcessMain {

	static public void main(String[] args) {
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		HAPDefinitionProcessSuite suite = HAPImporterProcessSuiteDefinition.readProcessSuiteDefinitionFromFile(HAPProcessMain.class.getResourceAsStream("expression.process"), runtimeEnvironment.getProcessManager());
		
		String id = "expressionProcessId";
		HAPExecutableProcess processed = HAPProcessorProcess.process(suite.getProcess(id), id, suite.getContext(), suite.getProcesses(), new HAPProcessContext(), runtimeEnvironment.getProcessManager(), new HAPEnvContextProcessor(HAPExpressionManager.dataTypeHelper, runtimeEnvironment.getRuntime(), runtimeEnvironment.getExpressionSuiteManager(), null));
		System.out.println(processed);
	}
	
}
