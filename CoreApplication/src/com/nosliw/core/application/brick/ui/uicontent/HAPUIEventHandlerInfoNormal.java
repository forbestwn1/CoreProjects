package com.nosliw.core.application.brick.ui.uicontent;

import java.util.Map;

import com.nosliw.core.application.common.event.HAPEventReferenceHandler;

public class HAPUIEventHandlerInfoNormal extends HAPUIEventHandlerInfo implements HAPWithUIId{

	//ui id that this event apply to
	private String m_uiId;

	public HAPUIEventHandlerInfoNormal() {
	}
	
	public HAPUIEventHandlerInfoNormal(String uiId, String event, HAPEventReferenceHandler handlerInfo) {
		super(event, handlerInfo);
		this.m_uiId = uiId;
	}

	@Override
	public String getUIId() {   return this.m_uiId;      }
	public void setUIId(String uiId) {    this.m_uiId = uiId;       }
	
	@Override
	public void parseContent(String content) {
		this.setHandlerInfo(HAPEventReferenceHandler.parseHandlerInfo(content));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UIID, this.m_uiId);
	}
}
