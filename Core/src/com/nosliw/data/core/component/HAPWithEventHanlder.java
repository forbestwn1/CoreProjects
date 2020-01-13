package com.nosliw.data.core.component;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithEventHanlder {

	@HAPAttribute
	public static String EVENTHANDLER = "eventHandler";
	
	public Set<HAPHandlerEvent> getEventHandlers();

	public void addEventHandler(HAPHandlerEvent eventHandler);
	
}
