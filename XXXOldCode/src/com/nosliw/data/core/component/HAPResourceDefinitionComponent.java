package com.nosliw.data.core.component;

import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPResourceDefinition1;
import com.nosliw.data.core.component.command.HAPDefinitionCommand;
import com.nosliw.data.core.component.event.HAPDefinitionEvent;

abstract public class HAPResourceDefinitionComponent extends HAPDefinitionEntityComponentImp implements HAPResourceDefinition1{

	public HAPResourceDefinitionComponent() {
	}

	public HAPResourceDefinitionComponent(String id) {
		this();
		this.setId(id);
	}
	
	@Override
	public String getEntityOrReferenceType() {   return HAPConstantShared.ENTITY;    }

	@Override
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
