package com.nosliw.data.core.cronjob;

import java.util.Calendar;
import java.util.Date;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPExecutablePollScheduleImp extends HAPSerializableImp implements HAPExecutablePollSchedule {

	@HAPAttribute
	public static String START = "start";
	
	@HAPAttribute
	public static String END = "end";
	
	@HAPAttribute
	public static String INTERVAL = "interval";
	
	//start time of schedule
	private Date m_start;
	
	//end time of schedule
	private Date m_end;
	
	//how often poll
	private int m_intervalSec;
	
	@Override
	public HAPInstancePollSchedule newPoll() {
		HAPInstancePollSchedule out = new HAPInstancePollSchedule();
		out.setPollTime(this.m_start);
		return out;
	}
	
	@Override
	public HAPInstancePollSchedule prepareForNextPoll(HAPInstancePollSchedule schedule) {
		Date pollTime = schedule.getPollTime();
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(pollTime);
		cal.add(Calendar.SECOND, m_intervalSec); 
		
		schedule.setPollTime(cal.getTime());
		
		return schedule;
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
//		this.m_start = new Date(jsonObj.optString(START));
//		this.m_end = new Date(jsonObj.optString(END));
		this.m_intervalSec = jsonObj.optInt(INTERVAL);
		return true;  
	}
}
