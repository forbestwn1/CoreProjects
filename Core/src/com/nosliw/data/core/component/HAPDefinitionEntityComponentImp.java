package com.nosliw.data.core.component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.component.command.HAPDefinitionCommand;
import com.nosliw.data.core.component.event.HAPDefinitionEvent;
import com.nosliw.data.core.service.use.HAPDefinitionServiceUse;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;
import com.nosliw.data.core.task.HAPDefinitionTaskSuiteImp;

abstract public class HAPDefinitionEntityComponentImp extends HAPDefinitionEntityComplexImp implements HAPDefinitionEntityComponent{

	private HAPDefinitionTaskSuite m_taskSuite;
	
	//used service
	private Map<String, HAPDefinitionServiceUse> m_serviceUse;
	
	private List<HAPDefinitionEvent> m_events;
	
	private List<HAPDefinitionCommand> m_commands;
	
	public HAPDefinitionEntityComponentImp() {
		this.m_serviceUse = new LinkedHashMap<String, HAPDefinitionServiceUse>();
		this.m_commands = new ArrayList<HAPDefinitionCommand>();
		this.m_events = new ArrayList<HAPDefinitionEvent>();
		this.m_taskSuite = new HAPDefinitionTaskSuiteImp(this);
	}

	public HAPDefinitionEntityComponentImp(String id) {
		this();
		this.setId(id);
	}
	
	@Override
	public HAPDefinitionTaskSuite getTaskSuite() {    return this.m_taskSuite;     }
	
	@Override
	public void addTask(HAPDefinitionTask task) {   this.m_taskSuite.addEntityElement(task);   }

	@Override
	public void setTaskSuite(HAPDefinitionTaskSuite suite) {  this.m_taskSuite = suite;    }
	
	@Override
	public HAPDefinitionServiceUse getService(String name) {   return this.m_serviceUse.get(name);   }
	
	@Override
	public Set<String> getAllServices(){   return this.m_serviceUse.keySet();     }
	
	@Override
	public void addService(HAPDefinitionServiceUse service) {   this.m_serviceUse.put(service.getName(), service);     }
	
	@Override
	public List<HAPDefinitionEvent> getEvents(){   return this.m_events;  }
	@Override
	public HAPDefinitionEvent getEvent(String eventName) {
		for(HAPDefinitionEvent eventDef : this.m_events) {
			if(eventName.equals(eventDef.getName())) {
				return eventDef;
			}
		}
		return null;
	}
	@Override
	public void addEvent(HAPDefinitionEvent event) {   this.m_events.add(event);   }

	@Override
	public List<HAPDefinitionCommand> getCommands(){   return this.m_commands;     }
	@Override
	public void addCommand(HAPDefinitionCommand command) {    this.m_commands.add(command);     }

	protected void cloneToComponent(HAPDefinitionEntityComponent component, boolean cloneValueStructure) {
		component.setId(this.getId());
		this.cloneToComplexResourceDefinition(component, cloneValueStructure);
		for(HAPDefinitionCommand command : this.m_commands) {
			component.addCommand(command.cloneCommandDefinition());
		}
		for(HAPDefinitionEvent event : this.m_events) {
			component.addEvent(event.cloneEventDefinition());
		}
		component.setTaskSuite(this.getTaskSuite().cloneTaskSuiteDefinition());
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(COMMAND, HAPUtilityJson.buildJson(this.m_commands, HAPSerializationFormat.JSON));
		jsonMap.put(EVENT, HAPUtilityJson.buildJson(this.m_events, HAPSerializationFormat.JSON));
		jsonMap.put(TASK, HAPUtilityJson.buildJson(this.m_taskSuite, HAPSerializationFormat.JSON));
	}
}
