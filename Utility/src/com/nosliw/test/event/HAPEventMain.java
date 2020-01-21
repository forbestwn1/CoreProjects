package com.nosliw.test.event;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.event.HAPDefinitionEventTask;
import com.nosliw.data.core.event.HAPExecutableEventTask;
import com.nosliw.data.core.event.HAPParserEventTask;
import com.nosliw.data.core.event.HAPProcessorEventTask;
import com.nosliw.data.core.event.HAPUtilityEventTask;
import com.nosliw.data.core.expression.HAPExpressionManager;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeProcessRhinoImp;
import com.nosliw.data.core.process.HAPProcessResultHandler;
import com.nosliw.data.core.process.HAPRuntimeProcess;

public class HAPEventMain {

	public static void main(String[] args) {
		
		HAPDefinitionEventTask eventTaskDef = HAPUtilityEventTask.getEventTaskDefinitionById("flightarrive", HAPParserEventTask.getInstance());
		
		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		HAPRuntimeProcess processRuntime = new HAPRuntimeProcessRhinoImp(runtimeEnvironment);
		
		HAPExecutableEventTask eventTaskExe = HAPProcessorEventTask.process(eventTaskDef, runtimeEnvironment.getProcessDefinitionManager(), HAPExpressionManager.dataTypeHelper, runtimeEnvironment.getRuntime(), runtimeEnvironment.getExpressionSuiteManager(), runtimeEnvironment.getServiceManager().getServiceDefinitionManager());
		processRuntime.executeEmbededProcess(eventTaskExe.getPollTask().getProcess(), eventTaskExe.getPollTask().getPollInput(), new HAPProcessResultHandler() {
			@Override
			public void onSuccess(String resultName, Map<String, HAPData> resultData) {
				System.out.println(resultName);
			}

			@Override
			public void onError(HAPServiceData serviceData) {
			}});
	}
	
	
}
