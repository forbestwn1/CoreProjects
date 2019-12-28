package com.nosliw.data.core.event;

import java.util.Map;

public class HAPEventTaskManager {

	private Map<String, HAPDefinitionEventTask> m_tasks;

	public HAPEventTask createEventTask(HAPDefinitionEventTask eventTaskDefinition) {
		return null;
	}
	
	public void removeEventTask(String taskId) {
		
	}
	
	//create source poll task
	public HAPPollTask generatePollTask(String taskId, HAPPollSchedule pollScedule) {
		return null;
	}
	
	//execute poll task
	public HAPPollTaskResult executePollTask(HAPPollTask pollTask) {
		return null;
	}
	
	//use handler to process event
	public void processEvent(HAPEvent event) {
		
	}
	
}
