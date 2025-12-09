package com.nosliw.core.application.common.event;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithEvents {

	@HAPAttribute
	public static String EVENT = "event";

	public Set<String> getEventNames();
	
	public HAPEventDefinition getEventDefinition(String name);
	
}
