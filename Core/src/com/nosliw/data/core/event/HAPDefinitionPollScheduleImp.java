package com.nosliw.data.core.event;

import java.util.Calendar;
import java.util.Date;

public class HAPDefinitionPollScheduleImp implements HAPDefinitionPollSchedule {

	//start time of schedule
	private Date m_start;
	
	//end time of schedule
	private Date m_end;
	
	//how often poll
	private int m_intervalSec;
	
	@Override
	public HAPPollSchedule newPoll() {
		HAPPollSchedule out = new HAPPollSchedule();
		out.setPollTime(this.m_start);
		return out;
	}
	
	@Override
	public HAPPollSchedule prepareForNextPoll(HAPPollSchedule schedule) {
		Date pollTime = schedule.getPollTime();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(pollTime);
		cal.add(Calendar.SECOND, m_intervalSec); 
		
		schedule.setPollTime(cal.getTime());
		
		return schedule;
	}
}
