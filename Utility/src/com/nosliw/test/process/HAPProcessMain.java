package com.nosliw.test.process;

import java.io.FileNotFoundException;
import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
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
		
		HAPDefinitionProcessSuite suite = HAPUtilityProcess.getProcessSuite("expression", runtimeEnvironment.getProcessDefinitionManager().getPluginManager()); 
		
		String id = "expressionProcessId";
		HAPExecutableProcess processExe = HAPProcessorProcess.process(id, suite, null, runtimeEnvironment.getProcessDefinitionManager(), new HAPRequirementContextProcessor(HAPExpressionManager.dataTypeHelper, runtimeEnvironment.getRuntime(), runtimeEnvironment.getExpressionSuiteManager(), runtimeEnvironment.getServiceManager().getServiceDefinitionManager(), null), new HAPProcessTracker());

		Map<String, HAPData> input = new LinkedHashMap<String, HAPData>();
		input.put("fromVar", HAPDataUtility.buildDataWrapper("#test.integer;1.0.0___7"));
		input.put("toVar", HAPDataUtility.buildDataWrapper("#test.integer;1.0.0___8"));
//		input.put("baseVar", HAPDataUtility.buildDataWrapper("#test.string;1.0.0___helloworld"));
		
		HAPRuntimeTaskExecuteProcessRhino task = new HAPRuntimeTaskExecuteProcessRhino(processExe, input);
		HAPServiceData out = runtimeEnvironment.getRuntime().executeTaskSync(task);
		
		System.out.println(out);
	}
	
}
