package com.nosliw.data.core.event;

import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPComponentImp;

//task definition 
//task definition describe how event trigure process 
public class HAPDefinitionEventTask extends HAPComponentImp{

	//task id
	private String m_id;
	
	//event info that trigue the process
	private HAPDefinitionEvent m_eventInfo;

	//event source (describle how event is trigued) that generate event
	private HAPDefinitionEventSource m_eventSourceInfo;
	
	//describle how to handle event
	private HAPDefinitionEventHandle m_eventProcess;

	public HAPDefinitionEventTask(String id) {
		super(id);
	}

	@Override
	public String getId() {    return this.m_id;    }
	
	public HAPDefinitionEvent getEventInfo() {   return this.m_eventInfo;  }
	
	public HAPDefinitionEventSource getEventSourceInfo() {   return this.m_eventSourceInfo;   }
	
	public HAPDefinitionEventHandle getProcess() {   return this.m_eventProcess;  }

	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		return null;
	}
	
}
