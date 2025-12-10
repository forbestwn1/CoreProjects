package com.nosliw.core.application.brick.ui.uicontent;

import java.util.Map;

import com.nosliw.core.application.common.event.HAPEventInfoHandler;

public class HAPUIHandlerEventTagCustom extends HAPUIHandlerEvent{

	public HAPUIHandlerEventTagCustom() {
		
	}
	
	public HAPUIHandlerEventTagCustom(String uiId, String event, HAPEventInfoHandler handlerInfo) {
		super(uiId, event, handlerInfo);
	}

	@Override
	public void parseContent(String content) {
		this.setHandlerInfo(HAPEventInfoHandler.parseHandlerInfo(content));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

}
