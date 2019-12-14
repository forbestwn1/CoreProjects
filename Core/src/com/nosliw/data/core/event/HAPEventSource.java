package com.nosliw.data.core.event;

public interface HAPEventSource {

	void registerListener(HAPEventSourceListener listener);
	
	void unregisterListener(HAPEventSourceListener listener);
	
}
