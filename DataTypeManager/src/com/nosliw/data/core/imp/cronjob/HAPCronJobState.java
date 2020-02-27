package com.nosliw.data.core.imp.cronjob;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.cronjob.HAPInstancePollSchedule;

public class HAPCronJobState {

	private String m_id;
	
	private String m_cronJobId;
	
	private HAPInstancePollSchedule m_schedule;
	
	private Map<String, HAPData> m_state;

	public HAPCronJobState(String id, String cronJobId, HAPInstancePollSchedule m_schedule, Map<String, HAPData> state) {
		
	}

	public String getCronJobId() {   return this.m_cronJobId;   }
	
	public Map<String, HAPData> getState(){    return this.m_state;     }
	
}
