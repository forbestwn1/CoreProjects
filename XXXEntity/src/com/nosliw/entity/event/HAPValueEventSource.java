package com.nosliw.entity.event;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/*
 * class that is envent source
 * 		manage listeners (add, remove, get, clear)
 * 		inform listeners
 * 		enable / disable envent 
 * 	listener is related with particular event type
 * 	so that when one event happen, only the listeners for that type of event will be informed
 * 		 
 */

public class HAPValueEventSource {

	private transient boolean m_eventEnabled = false;

	//event listener
	private transient Map<Integer, Set<HAPEventListener>> m_listeners = null;
	
	public HAPValueEventSource(){
		m_listeners = new LinkedHashMap<Integer, Set<HAPEventListener>>();
	}
	
	protected void cloneEventListenerTo(HAPValueEventSource source){
		source.m_listeners =  new LinkedHashMap<Integer, Set<HAPEventListener>>();
		Set<Integer> types = this.m_listeners.keySet();
		for(Integer type : types){
			for(HAPEventListener listener : this.m_listeners.get(type)){
				this.addListener(listener, type.intValue());
			}
		}
	}
	
	public void clearListener(){
		m_listeners.clear();
	}
	
	public void addListener(HAPEventListener listener, int eventtype){
		Set<HAPEventListener> listeners = this.m_listeners.get(new Integer(eventtype));
		if(listeners==null){
			listeners = new HashSet<HAPEventListener>();
			this.m_listeners.put(new Integer(eventtype), listeners);
		}
		listeners.add(listener);
	}
	
	public void removeListener(HAPEventListener listener)
	{
		Set<Integer> types = this.m_listeners.keySet();
		for(Integer type : types){
			this.m_listeners.get(type).remove(listener);
		}
	}
	
	public Integer[] getTypes(){
		return this.m_listeners.keySet().toArray(new Integer[0]);
	}
	
	public HAPEventListener[] getListeners(int type)
	{
		return this.m_listeners.get(new Integer(type)).toArray(new HAPEventListener[0]);
	}
	
	protected void informListener(HAPEvent event)
	{
		if(!this.isEventEnabled())  return;
		
		for(HAPEventListener listener : this.getListeners(event.m_eventType)){
			listener.onEvent(event);
		}
	}

	//yes, can send event 
	//no, cannot sent event
	public boolean isEventEnabled(){return this.m_eventEnabled;}
	public void setEventEnabled(boolean eventEnable){this.m_eventEnabled=eventEnable;}
}
