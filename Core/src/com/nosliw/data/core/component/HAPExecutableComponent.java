package com.nosliw.data.core.component;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.command.HAPExecutableCommand;
import com.nosliw.data.core.component.event.HAPExecutableEvent;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImpEntityInfo;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;
import com.nosliw.data.core.task.HAPExecutableTaskSuite;
import com.nosliw.data.core.valuestructure.HAPExecutableValueStructure;
import com.nosliw.data.core.valuestructure.HAPTreeNodeValueStructure;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructure;
import com.nosliw.data.core.valuestructure.HAPUtilityValueStructureScript;
import com.nosliw.data.core.valuestructure.HAPValueStructure;

@HAPEntityWithAttribute
public class HAPExecutableComponent extends HAPExecutableImpEntityInfo{

	@HAPAttribute
	public static String VALUESTRUCTURE = "valueStructure";
	
	@HAPAttribute
	public static String INITSCRIPT = "initScript";

	@HAPAttribute
	public static String TASK = "task";

	@HAPAttribute
	public static String EVENT = "event";

	@HAPAttribute
	public static String COMMAND = "command";

	@HAPAttribute
	public static final String SERVICE = "service";

	// hook up with real data during runtime
	private HAPTreeNodeValueStructure m_valueStructureNode;
	
	private HAPExecutableTaskSuite m_taskSuite;

	private Map<String, HAPExecutableEvent> m_events;
	
	private Map<String, HAPExecutableCommand> m_commands;
	
	//service requirement definition
	private Map<String, HAPExecutableServiceUse> m_services;

	public HAPExecutableComponent(HAPDefinitionComponent componentDefinition, String id) {
		super(componentDefinition);
		this.setId(id);
		this.m_valueStructureNode = new HAPTreeNodeValueStructure();
		this.m_valueStructureNode.setValueStructureWrapper(componentDefinition.getValueStructureWrapper());
		this.m_services = new LinkedHashMap<String, HAPExecutableServiceUse>();
		this.m_events = new LinkedHashMap<String, HAPExecutableEvent>();
		this.m_commands = new LinkedHashMap<String, HAPExecutableCommand>();
	}

	public void addServiceUse(String name, HAPExecutableServiceUse serviceDef) {   this.m_services.put(name, serviceDef);   }
	public Map<String, HAPExecutableServiceUse> getServiceUses(){  return this.m_services;   }
	public HAPExecutableServiceUse getServiceUse(String name) {   return this.m_services.get(name);  }

	public HAPTreeNodeValueStructure getValueStructureDefinitionNode() {   return this.m_valueStructureNode;   }
	public HAPValueStructure getValueStructure() {     return this.m_valueStructureNode.getValueStructureWrapper().getValueStructure();   }
	public HAPExecutableValueStructure getValueStructureExe() {	return HAPUtilityValueStructure.buildExecuatableValueStructure(this.m_valueStructureNode.getValueStructureWrapper().getValueStructure());}

//	public void setValueStructure(HAPValueStructure valueStructure) { 	this.m_valueStructureWrapper.setValueStructure(valueStructure);	}
	
	public HAPExecutableTaskSuite getTaskSuite() {    return this.m_taskSuite;    }
	public void setTaskSuite(HAPExecutableTaskSuite taskSuite) {    this.m_taskSuite = taskSuite;     }
	
	public Set<String> getEventNames(){    return this.m_events.keySet();     }
	public HAPExecutableEvent getEvent(String eventName) {    return this.m_events.get(eventName);    }
	public void addEvent(HAPExecutableEvent event) {     this.m_events.put(event.getName(), event);    }
	
	public Set<String> getCommandNames(){   return this.m_commands.keySet();   }
	public HAPExecutableCommand getCommand(String commandName) {    return this.m_commands.get(commandName);    }
	public void addCommand(HAPExecutableCommand command) {    this.m_commands.put(command.getName(), command);   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUESTRUCTURE, this.getValueStructureExe().toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(TASK, HAPJsonUtility.buildJson(this.m_taskSuite, HAPSerializationFormat.JSON));
		jsonMap.put(SERVICE, HAPJsonUtility.buildJson(this.m_services, HAPSerializationFormat.JSON));
		jsonMap.put(EVENT, HAPJsonUtility.buildJson(this.m_events, HAPSerializationFormat.JSON));
		jsonMap.put(COMMAND, HAPJsonUtility.buildJson(this.m_commands, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {	
		jsonMap.put(INITSCRIPT, HAPUtilityValueStructureScript.buildValueStructureInitScript(this.getValueStructureExe()).getScript());
		typeJsonMap.put(INITSCRIPT, HAPJsonTypeScript.class);
		
		if(this.m_taskSuite!=null) jsonMap.put(TASK, this.m_taskSuite.toResourceData(runtimeInfo).toString());
		
		{
			Map<String, String> serviceResourceMap = new LinkedHashMap<String, String>();
			for(String serviceName : this.m_services.keySet()) 	serviceResourceMap.put(serviceName, this.m_services.get(serviceName).toResourceData(runtimeInfo).toString());
			jsonMap.put(SERVICE, HAPJsonUtility.buildMapJson(serviceResourceMap));
		}
		{
			Map<String, String> eventResourceMap = new LinkedHashMap<String, String>();
			for(String eventName : this.m_events.keySet()) 	eventResourceMap.put(eventName, this.m_events.get(eventName).toResourceData(runtimeInfo).toString());
			jsonMap.put(EVENT, HAPJsonUtility.buildMapJson(eventResourceMap));
		}
		{		
			Map<String, String> commandResourceMap = new LinkedHashMap<String, String>();
			for(String commandName : this.m_commands.keySet()) 	commandResourceMap.put(commandName, this.m_commands.get(commandName).toResourceData(runtimeInfo).toString());
			jsonMap.put(COMMAND, HAPJsonUtility.buildMapJson(commandResourceMap));
		}
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		dependency.addAll(this.m_taskSuite.getResourceDependency(runtimeInfo, resourceManager));
		for(String serviceName : this.m_services.keySet()) 	dependency.addAll(this.m_services.get(serviceName).getResourceDependency(runtimeInfo, resourceManager));
		for(String eventName : this.m_events.keySet()) 	dependency.addAll(this.m_events.get(eventName).getResourceDependency(runtimeInfo, resourceManager));
		for(String commandName : this.m_commands.keySet()) 	dependency.addAll(this.m_commands.get(commandName).getResourceDependency(runtimeInfo, resourceManager));
	}

}
