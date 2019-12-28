package com.nosliw.data.core.event;

//task definition 
//task definition describe how event trigure process 
public class HAPDefinitionEventTask {

	//task id
	private String m_id;
	
	//event info that trigue the process
	private HAPDefinitionEvent m_eventInfo;

	//event source (describle how event is trigued) that generate event
	private HAPDefinitionEventSource m_eventSourceInfo;
	
	//describle how to handle event
	private HAPDefinitionEventHandle m_eventProcess;

	public String getId() {    return this.m_id;    }
	
	public HAPDefinitionEvent getEventInfo() {   return this.m_eventInfo;  }
	
	public HAPDefinitionEventSource getEventSourceInfo() {   return this.m_eventSourceInfo;   }
	
	public HAPDefinitionEventHandle getProcess() {   return this.m_eventProcess;  }
	
}
