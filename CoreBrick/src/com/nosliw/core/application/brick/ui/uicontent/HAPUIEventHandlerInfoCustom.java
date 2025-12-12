package com.nosliw.core.application.brick.ui.uicontent;

import java.util.Map;

import com.nosliw.core.application.common.event.HAPEventReferenceHandler;

public class HAPUIEventHandlerInfoCustom extends HAPUIEventHandlerInfo{

	public HAPUIEventHandlerInfoCustom() {
		
	}
	
	public HAPUIEventHandlerInfoCustom(String event, HAPEventReferenceHandler handlerInfo) {
		super(event, handlerInfo);
	}

	@Override
	public void parseContent(String content) {
		this.setHandlerInfo(HAPEventReferenceHandler.parseHandlerInfo(content));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

}
