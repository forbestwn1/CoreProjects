package com.nosliw.core.application.brick.ui.uicontent;

import java.util.Map;

public class HAPUIHandlerEventTagNormal extends HAPUIHandlerEvent{

	public HAPUIHandlerEventTagNormal() {
		
	}
	
	public HAPUIHandlerEventTagNormal(String uiId, String event, HAPUIInfoEventHandler handlerInfo) {
		super(uiId, event, handlerInfo);
	}

	@Override
	public void parseContent(String content) {
		this.setHandlerInfo(HAPUIInfoEventHandler.parseHandlerInfo(content));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}

}
