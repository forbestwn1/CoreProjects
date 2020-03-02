package com.nosliw.data.core.cronjob;

import java.time.Instant;

public class HAPInstancePollSchedule {

	//next poll time
	private Instant m_pollTime;
	
	public HAPInstancePollSchedule(Instant pollTime) {
		this.m_pollTime = pollTime;
	}
	
	public Instant getPollTime() {   return this.m_pollTime;   }
	
	public void setPollTime(Instant date) {   this.m_pollTime = date;    }
	
}
