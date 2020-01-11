package com.nosliw.data.core.event;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.process.HAPProcessResultHandler;
import com.nosliw.data.core.process.HAPRuntimeProcess;

//
public class HAPRuntimeEventProcess {

	private HAPRuntimeProcess m_prossRuntime; 

	//execute poll task
	public void executePollTask(HAPExecutableEventTask eventTask, HAPPollTaskResultHandler handler) {
		
		HAPExecutablePollTask pollTask = eventTask.getPollTask();
		this.m_prossRuntime.executeProcess(pollTask.getProcess(), pollTask.getPollInput(), new HAPProcessResultHandler() {
			@Override
			public void onSuccess(String resultName, Map<String, HAPData> resultData) {
//				handler.onSuccess(result);
			}

			@Override
			public void onError(HAPServiceData serviceData) {
				handler.onError(serviceData);
			}
		});
	}
	
	//use handler to process event
	public void executeEventHandle(HAPEvent event, HAPExecutableEventTask eventTask) {
		
	}
	

	
	public void removeEventTask(String taskId) {
		
	}
	
	//create source poll task
	public HAPExecutablePollTask generatePollTask(String taskId, HAPPollSchedule pollScedule) {
		return null;
	}
	

	
	public void poll() {
		
	}
	
	
	
}
