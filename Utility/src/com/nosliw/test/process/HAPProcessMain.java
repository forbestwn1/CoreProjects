package com.nosliw.test.process;

import java.io.FileNotFoundException;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPProcessorProcess;
import com.nosliw.data.core.process.HAPUtilityProcess;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteProcessRhino;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;

public class HAPProcessMain {

	static public void main(String[] args) throws FileNotFoundException {
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		
		HAPDefinitionProcessSuite suite = HAPUtilityProcess.getProcessSuite("expression", runtimeEnvironment.getProcessManager().getPluginManager()); 
		
		String id = "expressionProcessId";
		HAPExecutableProcess processExe = HAPProcessorProcess.process(id, suite, null, runtimeEnvironment.getProcessManager(), new HAPRequirementContextProcessor(HAPExpressionManager.dataTypeHelper, runtimeEnvironment.getRuntime(), runtimeEnvironment.getExpressionSuiteManager(), runtimeEnvironment.getServiceManager().getServiceDefinitionManager(), null), new HAPProcessTracker());

		HAPRuntimeTaskExecuteProcessRhino task = new HAPRuntimeTaskExecuteProcessRhino(processExe, null);
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);
		
		System.out.println(out);
	}
	
}
