package com.nosliw.data.core.task.resource;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPDefinitionEntityComponent;
import com.nosliw.data.core.component.HAPDefinitionEntityComponentImp;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;

//suite that contain multiple process
@HAPEntityWithAttribute
public class HAPResourceDefinitionTaskSuite extends HAPDefinitionEntityComponentImp implements HAPDefinitionTaskSuite{

	private Map<String, HAPDefinitionTask> m_tasks;
	
	public HAPResourceDefinitionTaskSuite() {
		this.m_tasks = new LinkedHashMap<String, HAPDefinitionTask>();
	}

	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASKSUITE;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	public Set<HAPDefinitionTask> getEntityElements() {  return new HashSet<HAPDefinitionTask>(this.m_tasks.values()); }

	@Override
	public HAPDefinitionTask getEntityElement(String id) {   return this.m_tasks.get(id);  }

	@Override
	public void addEntityElement(HAPDefinitionTask entityElement) {  this.m_tasks.put(entityElement.getId(), entityElement); }

	@Override
	public HAPDefinitionEntityComponent cloneComponent() {  return (HAPDefinitionEntityComponent)cloneTaskSuiteDefinition();  }

	@Override
	public HAPDefinitionTaskSuite cloneTaskSuiteDefinition() {
		HAPResourceDefinitionTaskSuite out = new HAPResourceDefinitionTaskSuite();
		this.cloneToComponent(out, true);
		for(String id : this.m_tasks.keySet()) {
			out.addEntityElement(this.m_tasks.get(id).cloneTaskDefinition());
		}
		return out;
	}

}
