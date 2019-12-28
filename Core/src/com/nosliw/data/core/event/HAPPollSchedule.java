package com.nosliw.data.core.event;

import java.util.Date;

public class HAPPollSchedule {

	//definition for poll schedule
	private HAPDefinitionPollSchedule m_definition;
	
	//task for event resource
	private String m_taskId;
	
	//the time after which the task should be poll
	//it could be updated after poll task to determine when is the next time to poll
	private Date nextTime;
	
	
	//update next time according to schedule definition
	public void updateNextTime() {
		
	}
	
}
