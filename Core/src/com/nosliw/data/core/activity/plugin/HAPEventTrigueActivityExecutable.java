package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;
import com.nosliw.data.core.activity.HAPExecutableActivity;
import com.nosliw.data.core.component.event.HAPExecutableEvent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPEventTrigueActivityExecutable extends HAPExecutableActivity{

	@HAPAttribute
	public static String EVENT = "event";

	private HAPExecutableEvent m_eventExe;
	
	public HAPEventTrigueActivityExecutable(String id, HAPDefinitionActivityNormal activityDef) {
		super(activityDef.getType(), id, activityDef);
	}

	public void setEvent(HAPExecutableEvent event) {   this.m_eventExe = event;  }
	public HAPExecutableEvent getEvent() {  return this.m_eventExe;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EVENT, HAPJsonUtility.buildJson(this.m_eventExe, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(EVENT, this.m_eventExe.toResourceData(runtimeInfo).toString());
	}	
}
