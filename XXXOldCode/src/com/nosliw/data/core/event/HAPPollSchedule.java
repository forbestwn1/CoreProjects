package com.nosliw.data.core.event;

import java.util.Date;

public class HAPPollSchedule {

	private String id;
	
	//task id this schedule belong to
	private String m_taskId;
	
	//schedule definition id 
	private String m_definitionId;
	
	//next poll time
	private Date m_pollTime;
	
	//current status of poll schedult
	private String m_status;
	
	public Date getPollTime() {   return this.m_pollTime;   }
	
	public void setPollTime(Date date) {   this.m_pollTime = date;    }
	
	
}
