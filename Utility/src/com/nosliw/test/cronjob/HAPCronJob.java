package com.nosliw.test.cronjob;

import com.nosliw.data.core.imp.runtime.js.rhino.HAPRuntimeEnvironmentImpRhino;
import com.nosliw.data.core.process.HAPRuntimeProcess;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeProcessRhinoImp;

public class HAPCronJob {

	public static void main(String[] args) {

		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
		HAPRuntimeProcess processRuntime = new HAPRuntimeProcessRhinoImp(runtimeEnvironment);
		
		
		
		
//		HAPDefinitionEventTask eventTaskDef = HAPUtilityEventTask.getEventTaskDefinitionById("flightarrive", HAPParserEventTask.getInstance());
//		
//		HAPRuntimeEnvironmentImpRhino runtimeEnvironment = new HAPRuntimeEnvironmentImpRhino();
//		HAPRuntimeProcess processRuntime = new HAPRuntimeProcessRhinoImp(runtimeEnvironment);
//		
//		HAPExecutableEventTask eventTaskExe = HAPProcessorEventTask.process(eventTaskDef, runtimeEnvironment.getProcessDefinitionManager(), HAPExpressionManager.dataTypeHelper, runtimeEnvironment.getRuntime(), runtimeEnvironment.getExpressionSuiteManager(), runtimeEnvironment.getServiceManager().getServiceDefinitionManager());
//		processRuntime.executeEmbededProcess(eventTaskExe.getPollTask().getProcess(), eventTaskExe.getPollTask().getPollInput(), new HAPProcessResultHandler() {
//			@Override
//			public void onSuccess(String resultName, Map<String, HAPData> resultData) {
//				System.out.println(resultName);
//				System.out.println(HAPJsonUtility.buildJson(resultData, HAPSerializationFormat.JSON));
//			}
//
//			@Override
//			public void onError(HAPServiceData serviceData) {
//			}});
	}
	
	
}
