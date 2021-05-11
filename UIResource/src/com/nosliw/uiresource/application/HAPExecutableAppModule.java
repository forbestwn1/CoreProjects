package com.nosliw.uiresource.application;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImpWrapper;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableGroupDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.uiresource.module.HAPExecutableModule;

@HAPEntityWithAttribute
public class HAPExecutableAppModule extends HAPEntityInfoImpWrapper implements HAPExecutable{

	@HAPAttribute
	public static final String MODULEDEFID = "moduleDefId";

	@HAPAttribute
	public static final String ROLE = "role";

	@HAPAttribute
	public static final String MODULE = "module";
	
	@HAPAttribute
	public static final String INPUTMAPPING = "inputMapping";
	
	@HAPAttribute
	public static final String OUTPUTMAPPING = "outputMapping";

	@HAPAttribute
	public static final String DATADEPENDENCY = "dataDependency";

	@HAPAttribute
	public static final String EVENTHANDLER = "eventHandler";
	
	private HAPExecutableModule m_module;
	
	private HAPExecutableGroupDataAssociation m_inputMapping;
	
	private HAPExecutableGroupDataAssociation m_outputMapping;

	private HAPDefinitionAppModule m_definition;
	
	private Set<String> m_dataDependency;
	
	private Map<String, HAPExecutableWrapperTask<HAPExecutableProcess>> m_eventHandlers;
	
	public HAPExecutableAppModule(HAPDefinitionAppModule def) {
		super(def);
		this.m_eventHandlers = new LinkedHashMap<String, HAPExecutableWrapperTask<HAPExecutableProcess>>();
		this.m_definition = def;
		this.m_inputMapping = new HAPExecutableGroupDataAssociation();
		this.m_outputMapping = new HAPExecutableGroupDataAssociation();
		this.m_dataDependency = new HashSet<String>();
	}

	public void setModule(HAPExecutableModule module) {  this.m_module = module;  }
	
	public void addEventHandler(String eventName, HAPExecutableWrapperTask<HAPExecutableProcess> eventHander) {   this.m_eventHandlers.put(eventName, eventHander);   }

	public void addInputDataAssociation(String name, HAPExecutableDataAssociation dataAssociation) {    
		this.m_inputMapping.addDataAssociation(name, dataAssociation);
		this.m_dataDependency.addAll(dataAssociation.getInput().getStructureNames());
	}
	
	public void addOutputDataAssociation(String name, HAPExecutableDataAssociation dataAssociation) {   this.m_outputMapping.addDataAssociation(name, dataAssociation);  }
	
	public void addDataDependency(String name) {   this.m_dataDependency.add(name);  }
	public void addDataDependency(Set<String> names) {   this.m_dataDependency.addAll(names);  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ROLE, this.m_definition.getRole());
		jsonMap.put(MODULE, HAPJsonUtility.buildJson(this.m_module, HAPSerializationFormat.JSON));
		jsonMap.put(INPUTMAPPING, HAPJsonUtility.buildJson(this.m_inputMapping, HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTMAPPING, HAPJsonUtility.buildJson(this.m_outputMapping, HAPSerializationFormat.JSON));
		jsonMap.put(DATADEPENDENCY, HAPJsonUtility.buildJson(this.m_dataDependency, HAPSerializationFormat.JSON));
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildJson(this.m_eventHandlers, HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildFullJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(MODULE, this.m_module.toResourceData(runtimeInfo).toString());
		jsonMap.put(INPUTMAPPING, this.m_inputMapping.toResourceData(runtimeInfo).toString());
		jsonMap.put(OUTPUTMAPPING, this.m_outputMapping.toResourceData(runtimeInfo).toString());

		Map<String, String> eventJsonMap = new LinkedHashMap<String, String>();
		for(String eventName :this.m_eventHandlers.keySet()) {	eventJsonMap.put(eventName, this.m_eventHandlers.get(eventName).toResourceData(runtimeInfo).toString());	}
		jsonMap.put(EVENTHANDLER, HAPJsonUtility.buildMapJson(eventJsonMap));

		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.addAll(this.m_module.getResourceDependency(runtimeInfo, resourceManager));
		for(HAPExecutableWrapperTask eventHandler : this.m_eventHandlers.values()) {	out.addAll(eventHandler.getResourceDependency(runtimeInfo, resourceManager));	}
		return out;
	}
}
