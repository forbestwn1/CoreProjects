package com.nosliw.data.core.cronjob;

public interface HAPExecutablePollSchedule {

	String getType();
	
	HAPInstancePollSchedule newPoll();
	
	HAPInstancePollSchedule prepareForNextPoll(HAPInstancePollSchedule schedule);
	
}
