package com.nosliw.core.application.brick.ui.uicontent;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public abstract class HAPUIHandlerEvent extends HAPSerializableImp{

	@HAPAttribute
	public static final String UIID = "uiId";
	@HAPAttribute
	public static final String EVENT = "event";
	@HAPAttribute
	public static final String HANDLERINFO = "handlerInfo";

	//ui id that this event apply to
	private String m_uiId;

	//event name
	private String m_event;
	
    //how to handle the event
	private HAPUIInfoEventHandler m_handlerInfo;
	
	public HAPUIHandlerEvent() {
		
	}
	
	public HAPUIHandlerEvent(String uiId, String event, HAPUIInfoEventHandler handlerInfo) {
		this.m_uiId = uiId;
		this.m_event = event;
		this.m_handlerInfo = handlerInfo;
	}
	
	public String getUIId() {   return this.m_uiId;      }
	public void setUIId(String uiId) {    this.m_uiId = uiId;       }
	
	public String getEvent() {     return this.m_event;     }
	public void setEvent(String event) {    this.m_event = event;     }
	
	public HAPUIInfoEventHandler getHandlerInfo() {    return this.m_handlerInfo;     }
	public void setHandlerInfo(HAPUIInfoEventHandler handlerInfo) {    this.m_handlerInfo = handlerInfo;     }

	public abstract void parseContent(String content);

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(UIID, this.m_uiId);
		jsonMap.put(EVENT, this.m_event);
		jsonMap.put(HANDLERINFO, this.m_handlerInfo.toStringValue(HAPSerializationFormat.JSON));
	}

}
