package com.nosliw.data.core.cronjob;

import java.util.Date;

public class HAPInstancePollSchedule {

	private String id;
	
	//job id this schedule belong to
	private String m_jobId;
	
	//next poll time
	private Date m_pollTime;
	
	//current status of poll schedult
	private String m_status;
	
	public Date getPollTime() {   return this.m_pollTime;   }
	
	public void setPollTime(Date date) {   this.m_pollTime = date;    }
	
	public void setJobId(String jobId) {   this.m_jobId = jobId;     }
	
}
