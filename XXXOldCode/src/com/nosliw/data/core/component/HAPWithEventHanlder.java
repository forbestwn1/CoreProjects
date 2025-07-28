package com.nosliw.data.core.component;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.component.event.HAPDefinitionHandlerEvent;

public interface HAPWithEventHanlder {

	@HAPAttribute
	public static String EVENTHANDLER = "eventHandler";
	
	public Set<HAPDefinitionHandlerEvent> getEventHandlers();

	public void addEventHandler(HAPDefinitionHandlerEvent eventHandler);
	
}
