package com.nosliw.data.core.event;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPComponentImp;

//task definition 
//task definition describe how event trigure process 
public class HAPDefinitionEventTask extends HAPComponentImp{

	@HAPAttribute
	public static String SOURCE = "source";

	@HAPAttribute
	public static String EVENTINFO = "eventInfo";

	@HAPAttribute
	public static String HANDLER = "handler";

	//event info that trigue the process
	private HAPDefinitionEvent m_eventInfo;

	//event source (describle how event is trigued) that generate event
	private HAPDefinitionEventSource m_eventSourceInfo;
	
	//describle how to handle event
	private HAPDefinitionEventHandle m_eventHandle;

	public HAPDefinitionEventTask(String id) {
		super(id, null);  //kkkk
	}

	public HAPDefinitionEvent getEventInfo() {   return this.m_eventInfo;  }
	public void setEventInfo(HAPDefinitionEvent eventInfo) {   this.m_eventInfo = eventInfo;    }
	
	public HAPDefinitionEventSource getEventSourceInfo() {   return this.m_eventSourceInfo;   }
	public void setEventSourceInfo(HAPDefinitionEventSource source) {    this.m_eventSourceInfo = source;     }
	
	public HAPDefinitionEventHandle getEventHandle() {   return this.m_eventHandle;  }
	public void setEventHandle(HAPDefinitionEventHandle eventHandler) {  this.m_eventHandle = eventHandler;    }

	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		return null;
	}
	
}
