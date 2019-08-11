package com.nosliw.uiresource.module;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.uiresource.common.HAPExecutableEventHandler;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;

@HAPEntityWithAttribute
public class HAPExecutableModuleUI extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String PAGE = "page";

	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";

	@HAPAttribute
	public static String OUTPUTMAPPING = "outputMapping";

	@HAPAttribute
	public static String EVENTHANDLER = "eventHandler";

	@HAPAttribute
	public static String PAGENAME = "pageName";
	
	private HAPDefinitionModuleUI m_moduleUIDefinition;
	
	private String m_id;

	private HAPExecutableUIUnitPage m_page;
	
	// hook up with real data during runtime
	private HAPExecutableDataAssociation m_inputMapping;
	private HAPExecutableDataAssociation m_outputMapping;
	
	private Map<String, HAPExecutableEventHandler> m_eventHandlers;
	
	public HAPExecutableModuleUI(HAPDefinitionModuleUI moduleUIDefinition, String id) {
		super(moduleUIDefinition);
		this.m_eventHandlers = new LinkedHashMap<String, HAPExecutableEventHandler>();
		this.m_moduleUIDefinition = moduleUIDefinition;
		this.m_id = id;
	}

	public void addEventHandler(String eventName, HAPExecutableEventHandler eventHander) {   this.m_eventHandlers.put(eventName, eventHander);   }
	
	public void setInputMapping(HAPExecutableDataAssociation contextMapping) {   this.m_inputMapping = contextMapping;	}
	public HAPExecutableDataAssociation getInputMapping() {   return this.m_inputMapping;   }

	public void setOutputMapping(HAPExecutableDataAssociation contextMapping) {   this.m_outputMapping = contextMapping;	}
	public HAPExecutableDataAssociation getOutputMapping() {   return this.m_outputMapping;   }

	public void setPage(HAPExecutableUIUnitPage page) {  this.m_page = page;   }
	public HAPExecutableUIUnitPage getPage() {  return this.m_page;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(PAGE, HAPJsonUtility.buildJson(this.m_page, HAPSerializationFormat.JSON));
		jsonMap.put(INPUTMAPPING, HAPJsonUtility.buildJson(this.m_inputMapping, HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTMAPPING, HAPJsonUtility.buildJson(this.m_outputMapping, HAPSerializationFormat.JSON));
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildJson(this.m_eventHandlers, HAPSerializationFormat.JSON));
		jsonMap.put(PAGENAME, this.m_moduleUIDefinition.getPage());
	}
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildFullJsonMap(jsonMap, typeJsonMap);

		jsonMap.put(PAGE, this.m_page.toResourceData(runtimeInfo).toString());
		
		Map<String, String> eventJsonMap = new LinkedHashMap<String, String>();
		for(String eventName :this.m_eventHandlers.keySet()) eventJsonMap.put(eventName, this.m_eventHandlers.get(eventName).toResourceData(runtimeInfo).toString());
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildMapJson(eventJsonMap));

		jsonMap.put(INPUTMAPPING, this.m_inputMapping.toResourceData(runtimeInfo).toString());
		jsonMap.put(OUTPUTMAPPING, this.m_outputMapping.toResourceData(runtimeInfo).toString());
		
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		out.addAll(this.m_page.getResourceDependency(runtimeInfo));
		for(HAPExecutableEventHandler eventHandler : this.m_eventHandlers.values()) 	out.addAll(eventHandler.getResourceDependency(runtimeInfo));
		out.addAll(this.m_inputMapping.getResourceDependency(runtimeInfo));
		out.addAll(this.m_outputMapping.getResourceDependency(runtimeInfo));
		return out;
	}
}
