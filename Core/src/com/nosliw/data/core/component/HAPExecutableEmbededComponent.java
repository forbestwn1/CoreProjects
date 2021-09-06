package com.nosliw.data.core.component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.event.HAPExecutableHandlerEvent;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableGroupDataAssociationForComponent;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

@HAPEntityWithAttribute
public class HAPExecutableEmbededComponent extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public static String COMPONENT = "component";

	@HAPAttribute
	public static String IN = "in";

	@HAPAttribute
	public static String OUT = "out";

	@HAPAttribute
	public static String EVENTHANDLER = "eventHandler";
	
	private HAPExecutableComponent m_component;
	
	// hook up with real data during runtime
	private HAPExecutableGroupDataAssociationForComponent m_inDataAssociations;
	private HAPExecutableGroupDataAssociationForComponent m_outDataAssociations;
	
	private Map<String, HAPExecutableHandlerEvent> m_eventHandlers;
	
	public HAPExecutableEmbededComponent(HAPEntityInfo entityDef) {
		super(entityDef);
		this.m_inDataAssociations = new HAPExecutableGroupDataAssociationForComponent();
		this.m_outDataAssociations = new HAPExecutableGroupDataAssociationForComponent();
		this.m_eventHandlers = new LinkedHashMap<String, HAPExecutableHandlerEvent>();
	}

	public HAPExecutableComponent getComponent() {    return this.m_component;     }
	public void setComponent(HAPExecutableComponent component) {    this.m_component = component;    }
	
	public void addInDataAssociation(HAPExecutableDataAssociation dataAssociation) {   this.m_inDataAssociations.addDataAssociation(dataAssociation);	}
	public HAPExecutableGroupDataAssociationForComponent getInDataAssociations() {   return this.m_inDataAssociations;   }

	public void addOutDataAssociation(HAPExecutableDataAssociation dataAssociation) {   this.m_outDataAssociations.addDataAssociation(dataAssociation);	}
	public HAPExecutableGroupDataAssociationForComponent getOutDataAssociations() {   return this.m_outDataAssociations;   }

	public void addEventHandler(HAPExecutableHandlerEvent eventHandler) {   this.m_eventHandlers.put(eventHandler.getEventName(), eventHandler);   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(IN, HAPJsonUtility.buildJson(this.m_inDataAssociations, HAPSerializationFormat.JSON));
		jsonMap.put(OUT, HAPJsonUtility.buildJson(this.m_outDataAssociations, HAPSerializationFormat.JSON));
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildJson(this.m_eventHandlers, HAPSerializationFormat.JSON));
		jsonMap.put(COMPONENT, HAPJsonUtility.buildJson(this.m_component, HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildFullJsonMap(jsonMap, typeJsonMap);

		Map<String, String> eventJsonMap = new LinkedHashMap<String, String>();
		for(String eventName :this.m_eventHandlers.keySet()) {	eventJsonMap.put(eventName, this.m_eventHandlers.get(eventName).toResourceData(runtimeInfo).toString());	}
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildMapJson(eventJsonMap));

		jsonMap.put(IN, this.m_inDataAssociations.toResourceData(runtimeInfo).toString());
		jsonMap.put(OUT, this.m_outDataAssociations.toResourceData(runtimeInfo).toString());
		
		jsonMap.put(COMPONENT, this.m_component.toResourceData(runtimeInfo).toString());

		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(this.m_inDataAssociations.getResourceDependency(runtimeInfo, resourceManager));
		out.addAll(this.m_outDataAssociations.getResourceDependency(runtimeInfo, resourceManager));
		for(HAPExecutableHandlerEvent eventHandler : this.m_eventHandlers.values()) {	out.addAll(eventHandler.getResourceDependency(runtimeInfo, resourceManager));	}
		out.addAll(this.m_component.getResourceDependency(runtimeInfo, resourceManager));
		return out;
	}
}
