package com.nosliw.core.application.brick.ui.uicontent;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.common.event.HAPEventReferenceHandler;

@HAPEntityWithAttribute
public abstract class HAPUIEventHandlerInfo extends HAPSerializableImp{

	@HAPAttribute
	public static final String EVENT = "event";
	@HAPAttribute
	public static final String HANDLERREFERENCE = "handlerReference";

	//event name
	private String m_event;
	
    //how to handle the event
	private HAPEventReferenceHandler m_handlerInfo;
	
	public HAPUIEventHandlerInfo() {
	}
	
	public HAPUIEventHandlerInfo(String event, HAPEventReferenceHandler handlerInfo) {
		this.m_event = event;
		this.m_handlerInfo = handlerInfo;
	}
	
	public String getEvent() {     return this.m_event;     }
	public void setEvent(String event) {    this.m_event = event;     }
	
	public HAPEventReferenceHandler getHandlerInfo() {    return this.m_handlerInfo;     }
	public void setHandlerInfo(HAPEventReferenceHandler handlerInfo) {    this.m_handlerInfo = handlerInfo;     }

	public abstract void parseContent(String content);

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EVENT, this.m_event);
		jsonMap.put(HANDLERREFERENCE, this.m_handlerInfo.toStringValue(HAPSerializationFormat.JSON));
	}

}
