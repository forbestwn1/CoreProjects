package com.nosliw.uiresource.module;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitResource;

public class HAPExecutableModuleUI extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String PAGE = "page";

	@HAPAttribute
	public static String CONTEXTMAPPING = "contextMapping";
	
	@HAPAttribute
	public static String EVENTHANDLER = "eventHandler";
	
	private HAPDefinitionModuleUI m_moduleUIDefinition;
	
	private String m_id;

	private HAPExecutableUIUnitResource m_page;
	
	// hook up with real data during runtime
	private HAPContext m_contextMapping;

	private Map<String, HAPExecutableModuleUIEventHandler> m_eventHandlers;
	
	public HAPExecutableModuleUI(HAPDefinitionModuleUI moduleUIDefinition, String id) {
		super(moduleUIDefinition);
		this.m_eventHandlers = new LinkedHashMap<String, HAPExecutableModuleUIEventHandler>();
		this.m_moduleUIDefinition = moduleUIDefinition;
		this.m_id = id;
	}

	public void addEventHandler(String eventName, HAPExecutableModuleUIEventHandler eventHander) {   this.m_eventHandlers.put(eventName, eventHander);   }
	
	public void setContextMapping(HAPContext contextMapping) {   this.m_contextMapping = contextMapping;   }
	public HAPContext getContextMapping() {   return this.m_contextMapping;   }
	
	public void setPage(HAPExecutableUIUnitResource page) {  this.m_page = page;   }
	public HAPExecutableUIUnitResource getPage() {  return this.m_page;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(PAGE, HAPJsonUtility.buildJson(this.m_page, HAPSerializationFormat.JSON));
		jsonMap.put(CONTEXTMAPPING, HAPJsonUtility.buildJson(this.m_contextMapping, HAPSerializationFormat.JSON));
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildJson(this.m_eventHandlers, HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildFullJsonMap(jsonMap, typeJsonMap);

		jsonMap.put(PAGE, this.m_page.toResourceData(runtimeInfo).toString());
		
		Map<String, String> eventJsonMap = new LinkedHashMap<String, String>();
		for(String eventName :this.m_eventHandlers.keySet()) {
			eventJsonMap.put(eventName, this.m_eventHandlers.get(eventName).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildMapJson(eventJsonMap));
		
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		// TODO Auto-generated method stub
		return null;
	}

}
