package com.nosliw.data.core.activity.resource;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivitySuite;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPComponentImp;

//suite that contain multiple process
@HAPEntityWithAttribute
public class HAPResourceDefinitionActivitySuite extends HAPComponentImp implements HAPDefinitionActivitySuite{

	private Map<String, HAPDefinitionActivity> m_activities;
	
	public HAPResourceDefinitionActivitySuite() {
		this.m_activities = new LinkedHashMap<String, HAPDefinitionActivity>();
	}

	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_ACTIVITYSUITE;  }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	

	@Override
	public Set<HAPDefinitionActivity> getEntityElements() {  return new HashSet<HAPDefinitionActivity>(this.m_activities.values()); }

	@Override
	public HAPDefinitionActivity getEntityElement(String id) {   return this.m_activities.get(id);  }

	@Override
	public void addEntityElement(HAPDefinitionActivity entityElement) {  this.m_activities.put(entityElement.getId(), entityElement); }

	@Override
	public HAPComponent cloneComponent() {  return (HAPComponent)cloneActivitySuiteDefinition();  }

	@Override
	public HAPDefinitionActivitySuite cloneActivitySuiteDefinition() {
		HAPResourceDefinitionActivitySuite out = new HAPResourceDefinitionActivitySuite();
		this.cloneToComponent(out, true);
		for(String id : this.m_activities.keySet()) {
			out.addEntityElement(this.m_activities.get(id).cloneActivityDefinition());
		}
		return out;
	}

}
