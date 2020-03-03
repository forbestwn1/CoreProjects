package com.nosliw.data.core.cronjob;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPExecutablePollSchedule {

	@HAPAttribute
	public static String TYPE = "type";
	
	String getType();
	
	HAPInstancePollSchedule newPoll();
	
	HAPInstancePollSchedule prepareForNextPoll(HAPInstancePollSchedule schedule);
	
}
