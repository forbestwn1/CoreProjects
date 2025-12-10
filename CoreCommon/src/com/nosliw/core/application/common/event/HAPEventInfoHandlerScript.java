package com.nosliw.core.application.common.event;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPEventInfoHandlerScript extends HAPEventInfoHandler{

	@HAPAttribute
	public static final String FUNCTIONNAME = "functionName";

	private String m_functionName;
	
	@Override
	public String getHandlerType() {   return HAPConstantShared.EVENT_HANDLERTYPE_SCRIPT;   }

	public String getFunctionName() {     return this.m_functionName;     }
	public void setFunctionName(String functionName) {      this.m_functionName = functionName;      }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(FUNCTIONNAME, this.m_functionName);
	}
}
