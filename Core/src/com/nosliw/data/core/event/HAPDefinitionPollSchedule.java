package com.nosliw.data.core.event;

public interface HAPDefinitionPollSchedule {

	HAPPollSchedule newPoll();
	
	HAPPollSchedule prepareForNextPoll(HAPPollSchedule schedule);
	
}
