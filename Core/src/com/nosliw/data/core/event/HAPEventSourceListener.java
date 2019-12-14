package com.nosliw.data.core.event;

public interface HAPEventSourceListener {

	void onEvent(HAPEvent eventData);
	
	void onDestroy();
	
}
