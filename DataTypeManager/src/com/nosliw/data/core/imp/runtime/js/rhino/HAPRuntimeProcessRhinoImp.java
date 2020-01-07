package com.nosliw.data.core.imp.runtime.js.rhino;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPProcessResultHandler;
import com.nosliw.data.core.process.HAPRuntimeProcess;
import com.nosliw.data.core.runtime.js.rhino.task.HAPRuntimeTaskExecuteProcessRhino;

public class HAPRuntimeProcessRhinoImp implements HAPRuntimeProcess{

	private HAPRuntimeEnvironmentImpRhino m_runtimeEnvironment;
	
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
}
