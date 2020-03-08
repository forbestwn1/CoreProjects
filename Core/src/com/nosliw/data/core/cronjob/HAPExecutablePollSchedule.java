package com.nosliw.data.core.cronjob;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializable;

public interface HAPExecutablePollSchedule extends HAPSerializable{

	@HAPAttribute
	public static String TYPE = "type";
	
	String getType();
	
	HAPInstancePollSchedule newPoll();
	
	HAPInstancePollSchedule prepareForNextPoll(HAPInstancePollSchedule schedule);
	
}
