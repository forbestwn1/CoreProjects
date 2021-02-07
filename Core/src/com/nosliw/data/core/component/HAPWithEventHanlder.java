package com.nosliw.data.core.component;

import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.handler.HAPHandler;

public interface HAPWithEventHanlder {

	@HAPAttribute
	public static String EVENTHANDLER = "eventHandler";
	
	public Set<HAPHandler> getEventHandlers();

	public void addEventHandler(HAPHandler eventHandler);
	
}
