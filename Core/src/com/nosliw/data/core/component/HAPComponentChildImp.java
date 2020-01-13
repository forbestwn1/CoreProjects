package com.nosliw.data.core.component;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPComponentChildImp extends HAPEntityInfoWritableImp implements HAPWithNameMapping, HAPWithEventHanlder{

	//mapping reference name from attachment to internal name
	private HAPNameMapping m_nameMapping;
	
	//event handlers
	private Set<HAPHandlerEvent> m_eventHandlers;
	
	public HAPComponentChildImp() {
		this.m_nameMapping = new HAPNameMapping();
		this.m_eventHandlers = new HashSet<HAPHandlerEvent>();
	}
	
	public void setNameMapping(HAPNameMapping nameMapping) {   if(nameMapping!=null)  this.m_nameMapping = nameMapping;  }
	@Override
	public HAPNameMapping getNameMapping() {    return this.m_nameMapping;   }

	@Override
	public Set<HAPHandlerEvent> getEventHandlers(){   return this.m_eventHandlers;   }
	@Override
	public void addEventHandler(HAPHandlerEvent eventHandler) {  this.m_eventHandlers.add(eventHandler);   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildJson(this.m_eventHandlers, HAPSerializationFormat.JSON));
	}

}
