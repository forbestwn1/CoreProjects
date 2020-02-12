package com.nosliw.data.core.imp.runtime.js.rhino;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.HAPDataUtility;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPProcessResultHandler;
import com.nosliw.data.core.process.HAPRuntimeProcess;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteProcessEmbededRhino;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteProcessRhino;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;

public class HAPRuntimeProcessRhinoImp implements HAPRuntimeProcess{

	private HAPRuntimeEnvironment m_runtimeEnvironment;
	
	public HAPRuntimeProcessRhinoImp(HAPRuntimeEnvironment runtime) {
		this.m_runtimeEnvironment = runtime;
	}
	
	@Override
	public void executeProcess(HAPExecutableProcess processExe, Map<String, HAPData> input, HAPProcessResultHandler resultHandler) {
		HAPRuntimeTaskExecuteProcessRhino task = new HAPRuntimeTaskExecuteProcessRhino(processExe, input);
		HAPServiceData out = m_runtimeEnvironment.getRuntime().executeTaskSync(task);
		if(out.isSuccess()) {
			resultHandler.onSuccess("", null);
		}
		else {
			
		}
	}

	@Override
	public void executeEmbededProcess(HAPExecutableWrapperTask<HAPExecutableProcess> processExe,
			Map<String, HAPData> input, HAPProcessResultHandler resultHandler) {
		HAPRuntimeTaskExecuteProcessEmbededRhino task = new HAPRuntimeTaskExecuteProcessEmbededRhino(processExe, input);
		HAPServiceData out = m_runtimeEnvironment.getRuntime().executeTaskSync(task);
		if(out.isSuccess()) {
			JSONObject dataJsonObj = (JSONObject)out.getData();
			resultHandler.onSuccess(dataJsonObj.getString("resultName"), HAPDataUtility.buildDataWrapperMapFromJson(dataJsonObj.getJSONObject("resultValue")));
		}
		else {
			
		}
	}
}
