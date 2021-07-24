package com.nosliw.data.core.component;

import java.util.List;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithEvent {

	@HAPAttribute
	public static String EVENT = "event";

	List<HAPDefinitionEvent> getEvents();

	void addEvent(HAPDefinitionEvent event);
}
