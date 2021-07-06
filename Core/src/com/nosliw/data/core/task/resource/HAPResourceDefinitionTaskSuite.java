package com.nosliw.data.core.task.resource;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentImp;
import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;

//suite that contain multiple process
@HAPEntityWithAttribute
public class HAPResourceDefinitionTaskSuite extends HAPComponentImp implements HAPDefinitionTaskSuite{

	private Map<String, HAPDefinitionTask> m_activities;
	
	public HAPResourceDefinitionTaskSuite() {
		this.m_activities = new LinkedHashMap<String, HAPDefinitionTask>();
	}

	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_TASKSUITE;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	

	@Override
	public Set<HAPDefinitionTask> getEntityElements() {  return new HashSet<HAPDefinitionTask>(this.m_activities.values()); }

	@Override
	public HAPDefinitionTask getEntityElement(String id) {   return this.m_activities.get(id);  }

	@Override
	public void addEntityElement(HAPDefinitionTask entityElement) {  this.m_activities.put(entityElement.getId(), entityElement); }

	@Override
	public HAPComponent cloneComponent() {  return (HAPComponent)cloneTaskSuiteDefinition();  }

	@Override
	public HAPDefinitionTaskSuite cloneTaskSuiteDefinition() {
		HAPResourceDefinitionTaskSuite out = new HAPResourceDefinitionTaskSuite();
		this.cloneToComponent(out, true);
		for(String id : this.m_activities.keySet()) {
			out.addEntityElement(this.m_activities.get(id).cloneTaskDefinition());
		}
		return out;
	}

}
