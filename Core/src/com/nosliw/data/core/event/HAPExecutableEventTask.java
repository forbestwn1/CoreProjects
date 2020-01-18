package com.nosliw.data.core.event;

public class HAPExecutableEventTask {

	private String m_id;
	
	private String m_status;
	
	private HAPPollSchedule m_pollSchedule;

	private HAPExecutablePollTask m_pollTask;
	
	private HAPExecutableEventHandler m_eventHandler;

	private HAPDefinitionEventTask m_definition;
	
	public HAPExecutableEventTask(HAPDefinitionEventTask definition) {
		this.m_definition = definition;
	}
	
	public String getStatus() {   return this.m_status;     }

	public void setPollTask(HAPExecutablePollTask pollTask) {  this.m_pollTask = pollTask;   }
	public HAPExecutablePollTask getPollTask() {    return this.m_pollTask;    }
	
	public HAPPollSchedule getPollSchedule() {   return this.m_pollSchedule;   }
	public void setPollSchedule(HAPPollSchedule schedule) {   this.m_pollSchedule = schedule;    }
	
	public void setEventHandler(HAPExecutableEventHandler eventHandler) {   this.m_eventHandler = eventHandler;      }
	public HAPExecutableEventHandler getEventHandler() {   return this.m_eventHandler;  }
	
	
}
