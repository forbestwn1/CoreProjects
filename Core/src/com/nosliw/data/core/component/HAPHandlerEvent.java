package com.nosliw.data.core.component;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.handler.HAPHandlerStep;

@HAPEntityWithAttribute
public class HAPHandlerEvent{

	private String m_eventName;
	
	private List<HAPHandlerStep> m_handers;
	
	public HAPHandlerEvent(String eventName) {
		this.m_eventName = eventName;
		this.m_handers = new ArrayList<HAPHandlerStep>();
	}
	
	public void addHandler(HAPHandlerStep handler) {    this.m_handers.add(handler);     }
	public List<HAPHandlerStep> getHandlers(){   return this.m_handers;  	}
	
	public String getEventName() {   return this.m_eventName;    }
	
	
}
