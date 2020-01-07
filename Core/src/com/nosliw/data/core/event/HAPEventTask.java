package com.nosliw.data.core.event;

public class HAPEventTask {

	private String m_id;
	
	private String m_status;
	
	private HAPPollSchedule m_pollSchedule;

	private HAPPollTask m_pollTask;
	
	private HAPEventHandler m_eventHandler;
	
	public String getStatus() {   return this.m_status;     }
	
}
