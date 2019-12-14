package com.nosliw.data.core.event;

//task definition 
//task definition describe how event trigure process 
public class HAPEventTaskInfo {

	//task id
	private String m_id;
	
	//event info that trigue the process
	private HAPEventInfo m_eventInfo;

	//event source (describle how event is trigued)
	private HAPEventSource m_eventSource;
	
	//process event
	private HAPDefinitionEventProcess m_eventProcess;

	public String getId() {    return this.m_id;    }
	
	public HAPEventInfo getEventInfo() {   return this.m_eventInfo;  }
	
	public HAPEventSource getEventSource() {   return this.m_eventSource;   }
	
	public HAPDefinitionEventProcess getProcess() {   return this.m_eventProcess;  }
	
}
