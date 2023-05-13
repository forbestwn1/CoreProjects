package com.nosliw.data.core.component.event;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.handler.HAPExecutableHandler;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

@HAPEntityWithAttribute
public class HAPExecutableHandlerEvent extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public static String EVENTNAME = "eventName";

	@HAPAttribute
	public static String IN = "in";

	@HAPAttribute
	public static String HANDLER = "handler";

	private String m_eventName;
	
	private HAPExecutableDataAssociation m_inDataAssociation;
	
	private HAPExecutableHandler m_handler;
	
	public HAPExecutableHandlerEvent() {
		
	}
	
	public String getEventName() {   return this.m_eventName;    }
	
	public void setInDataAssociation(HAPExecutableDataAssociation dataAssociation) {    this.m_inDataAssociation = dataAssociation;    }
	
	public void setEventName(String eventName) {    this.m_eventName = eventName;     }
	
	public void setHandler(HAPExecutableHandler handler) {    this.m_handler = handler;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EVENTNAME, this.m_eventName);
		jsonMap.put(HANDLER, HAPUtilityJson.buildJson(this.m_handler, HAPSerializationFormat.JSON));
		jsonMap.put(IN, HAPUtilityJson.buildJson(this.m_inDataAssociation, HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildFullJsonMap(jsonMap, typeJsonMap);

		jsonMap.put(IN, this.m_inDataAssociation.toResourceData(runtimeInfo).toString());
		jsonMap.put(HANDLER, this.m_handler.toResourceData(runtimeInfo).toString());

		return HAPResourceDataFactory.createJSValueResourceData(HAPUtilityJson.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		super.buildResourceDependency(dependency, runtimeInfo, resourceManager);
		this.buildResourceDependencyForExecutable(dependency, m_inDataAssociation, runtimeInfo, resourceManager);
		this.buildResourceDependencyForExecutable(dependency, m_handler, runtimeInfo, resourceManager);
	}

}
