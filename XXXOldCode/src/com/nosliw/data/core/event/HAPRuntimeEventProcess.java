package com.nosliw.data.core.event;

import com.nosliw.data.core.process1.HAPRuntimeProcess;

//
public class HAPRuntimeEventProcess {

	private HAPRuntimeProcess m_prossRuntime; 

	//execute poll task
	public void executePollTask(HAPExecutableEventTask eventTask, HAPPollTaskResultHandler handler) {
		
//		HAPExecutablePollTask pollTask = eventTask.getPollTask();
//		this.m_prossRuntime.executeEmbededProcess(pollTask.getProcess(), pollTask.getPollInput(), new HAPProcessResultHandler() {
//			@Override
//			public void onSuccess(String resultName, Map<String, HAPData> resultData) {
//				// TODO Auto-generated method stub
//				
//			}
//
//			@Override
//			public void onError(HAPServiceData serviceData) {
//				handler.onError(serviceData);
//			}
//		});
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
