package com.nosliw.core.application.common.event;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithEvents {

	@HAPAttribute
	public static String EVENT = "event";
	
	public List<HAPEventDefinition> getEvents();

}
