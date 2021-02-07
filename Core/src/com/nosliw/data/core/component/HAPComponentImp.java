package com.nosliw.data.core.component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.handler.HAPHandler;

abstract public class HAPComponentImp extends HAPResourceDefinitionComplexImp implements HAPComponent{

	//lifecycle definition
	private Set<HAPHandlerLifecycle> m_lifecycleAction;
	
	//event handlers
	private Set<HAPHandler> m_eventHandlers;

	public HAPComponentImp() {
		this.m_lifecycleAction = new HashSet<HAPHandlerLifecycle>();
		this.m_eventHandlers = new HashSet<HAPHandler>();
	}

	public HAPComponentImp(String id) {
		this();
		this.setId(id);
	}
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstant.ENTITY;    }

	@Override
	public Set<HAPHandlerLifecycle> getLifecycleAction(){    return this.m_lifecycleAction;    }
	@Override
	public void addLifecycleAction(HAPHandlerLifecycle lifecycleAction) {    this.m_lifecycleAction.add(lifecycleAction);    }
 	
	@Override
	public Set<HAPHandler> getEventHandlers(){   return this.m_eventHandlers;   }
	@Override
	public void addEventHandler(HAPHandler eventHandler) {  this.m_eventHandlers.add(eventHandler);   }

	protected void cloneToComponent(HAPComponent component) {
		component.setId(this.getId());
		this.cloneToComplexResourceDefinition(component);
		for(HAPHandlerLifecycle handler : this.m_lifecycleAction) {
			component.addLifecycleAction(handler.cloneLifecycleHander());
		}
		for(HAPHandler handler : this.m_eventHandlers) {
			component.addEventHandler(handler.cloneHandler());
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(LIFECYCLE, HAPJsonUtility.buildJson(this.m_lifecycleAction, HAPSerializationFormat.JSON));
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildJson(this.m_eventHandlers, HAPSerializationFormat.JSON));
	}
}
