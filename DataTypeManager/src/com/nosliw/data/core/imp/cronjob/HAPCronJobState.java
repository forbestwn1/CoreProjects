package com.nosliw.data.core.imp.cronjob;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.core.data.HAPData;
import com.nosliw.data.core.cronjob.HAPInstancePollSchedule;

public class HAPCronJobState {

	private String m_id;
	
	private String m_cronJobId;
	
	private HAPInstancePollSchedule m_schedule;
	
	private Map<String, HAPData> m_state;

	public HAPCronJobState(String id, String cronJobId, HAPInstancePollSchedule schedule, Map<String, HAPData> state) {
		this.m_id = id;
		this.m_cronJobId = cronJobId;
		this.m_schedule = schedule;
		if(state!=null)    this.m_state = state;
		else this.m_state = new LinkedHashMap<String, HAPData>();
	}

	public String getId() {      return this.m_id;      }
	public void setId(String id) {   this.m_id = id;     }

	public String getCronJobId() {   return this.m_cronJobId;   }
	public void setCronJobId(String cronJobId) {    this.m_cronJobId = cronJobId;     }
	
	public Map<String, HAPData> getState(){    return this.m_state;     }
	public void setState(Map<String, HAPData> state) {    
		if(state!=null)   this.m_state = state;      
	}
	
	public HAPInstancePollSchedule getSchedule() {     return this.m_schedule;     }
	public void setSchedule(HAPInstancePollSchedule schedule) {     this.m_schedule = schedule;      }
	
}
