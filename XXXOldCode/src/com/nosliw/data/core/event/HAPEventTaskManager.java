package com.nosliw.data.core.event;

import java.util.Map;

public class HAPEventTaskManager {

	private Map<String, HAPDefinitionEventTask> m_tasks;

	public HAPExecutableEventTask getEventTask(String eventTaskDefId) {
		HAPDefinitionEventTask eventTaskDefinition = HAPUtilityEventTask.getEventTaskDefinitionById(eventTaskDefId, HAPParserEventTask.getInstance());
		
		
		return null;
	}

}
