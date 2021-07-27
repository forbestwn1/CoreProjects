package com.nosliw.data.core.component.event;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithEvent {

	@HAPAttribute
	public static String EVENT = "event";

	List<HAPDefinitionEvent> getEvents();

	HAPDefinitionEvent getEvent(String eventName);
	
	void addEvent(HAPDefinitionEvent event);
}
