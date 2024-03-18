package com.nosliw.data.core.runtime.js.imp.rhino;

public class HAPRuntimeProcessRhinoImp {//implements HAPRuntimeProcess{
/*
	private HAPRuntimeEnvironment m_runtimeEnvironment;
	
	public HAPRuntimeProcessRhinoImp(HAPRuntimeEnvironment runtime) {
		this.m_runtimeEnvironment = runtime;
	}
	
	@Override
	public void executeProcess(HAPExecutableProcess processExe, HAPContextData input, HAPProcessResultHandler resultHandler, HAPResourceManagerRoot resourceManager) {
		HAPRuntimeTaskExecuteProcessRhino task = new HAPRuntimeTaskExecuteProcessRhino(processExe, input, resourceManager);
		HAPServiceData out = m_runtimeEnvironment.getRuntime().executeTaskSync(task);
		if(out.isSuccess()) {
			resultHandler.onSuccess("", null);
		}
		else {
			
		}
	}

	@Override
	public void executeEmbededProcess(HAPExecutableWrapperTask<HAPExecutableProcess> processExe,
			HAPContextData parentContext, HAPProcessResultHandler resultHandler, HAPResourceManagerRoot resourceManager) {
		HAPRuntimeTaskExecuteProcessEmbededRhino task = new HAPRuntimeTaskExecuteProcessEmbededRhino(processExe, parentContext, resourceManager);
		HAPServiceData out = m_runtimeEnvironment.getRuntime().executeTaskSync(task);
		if(out.isSuccess()) {
			JSONObject dataJsonObj = (JSONObject)out.getData();
			resultHandler.onSuccess(dataJsonObj.getString("resultName"), HAPUtilityData.buildDataWrapperMapFromJson(dataJsonObj.getJSONObject("resultValue")));
		}
		else {
			
		}
	}

	@Override
	public HAPServiceData executeProcess(HAPExecutableProcess process, HAPContextData input, HAPResourceManagerRoot resourceManager) {
		HAPRuntimeTaskExecuteProcessRhino task = new HAPRuntimeTaskExecuteProcessRhino(process, input, resourceManager);
		return this.m_runtimeEnvironment.getRuntime().executeTaskSync(task);
	}

	@Override
	public HAPServiceData executeEmbededProcess(HAPExecutableWrapperTask<HAPExecutableProcess> process, HAPContextData parentContext, HAPResourceManagerRoot resourceManager) {
		HAPRuntimeTaskExecuteProcessEmbededRhino task = new HAPRuntimeTaskExecuteProcessEmbededRhino(process, parentContext, resourceManager);
		return this.m_runtimeEnvironment.getRuntime().executeTaskSync(task);
	}
*/	
}
