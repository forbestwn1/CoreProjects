package com.nosliw.data.core.event;


//
public class HAPRuntimeEventProcess {

	//execute poll task
	public HAPPollTaskResult executePollTask(HAPExecutableEventTask eventTask) {
		
		
		return null;
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
