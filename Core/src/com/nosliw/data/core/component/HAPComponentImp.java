package com.nosliw.data.core.component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;

abstract public class HAPComponentImp extends HAPResourceDefinitionComplexImp implements HAPComponent{

	private String m_id;

	//lifecycle definition
	private Set<HAPHandlerLifecycle> m_lifecycleAction;
	
	//event handlers
	private Set<HAPHandlerEvent> m_eventHandlers;

	private HAPDefinitionProcessSuite m_processSuite;
	
	public HAPComponentImp() {
		this.m_lifecycleAction = new HashSet<HAPHandlerLifecycle>();
		this.m_eventHandlers = new HashSet<HAPHandlerEvent>();
	}

	public HAPComponentImp(String id) {
		this.m_id = id;
	}
	
	@Override
	public HAPDefinitionProcessSuite getProcessSuite() {		return this.m_processSuite;	}
	@Override
	public void setProcessSuite(HAPDefinitionProcessSuite processSuite) {  this.m_processSuite = processSuite;   }

	
	@Override
	public String getId() {   return this.m_id;   }
	@Override
	public void setId(String id) {  this.m_id = id;   }
 	 
	@Override
	public Set<HAPHandlerLifecycle> getLifecycleAction(){    return this.m_lifecycleAction;    }
	@Override
	public void addLifecycleAction(HAPHandlerLifecycle lifecycleAction) {    this.m_lifecycleAction.add(lifecycleAction);    }
 	
	@Override
	public Set<HAPHandlerEvent> getEventHandlers(){   return this.m_eventHandlers;   }
	@Override
	public void addEventHandler(HAPHandlerEvent eventHandler) {  this.m_eventHandlers.add(eventHandler);   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(LIFECYCLE, HAPJsonUtility.buildJson(this.m_lifecycleAction, HAPSerializationFormat.JSON));
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildJson(this.m_eventHandlers, HAPSerializationFormat.JSON));
	}
}
