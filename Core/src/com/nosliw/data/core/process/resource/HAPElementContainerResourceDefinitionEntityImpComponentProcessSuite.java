package com.nosliw.data.core.process.resource;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.HAPComponent;
import com.nosliw.data.core.component.HAPElementContainerResourceDefinition;
import com.nosliw.data.core.component.HAPElementContainerResourceDefinitionEntityImpComponent;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.resource.HAPResourceDependency;

/**
 * Task is unit that can execute 
 * All information required when describe a task
 * Task is a sequence of steps
 */
@HAPEntityWithAttribute
public class HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite extends HAPElementContainerResourceDefinitionEntityImpComponent implements HAPDefinitionProcess{ 

	@HAPAttribute
	public static String ACTIVITY = "activity";

	private Map<String, HAPDefinitionActivity> m_activities;

	//dependent resources
	private Set<HAPResourceDependency> m_requiredResources;

	public HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite(){
		super();
		this.m_activities = new LinkedHashMap<String, HAPDefinitionActivity>();
		this.m_requiredResources = new HashSet<HAPResourceDependency>();
	}
 
	@Override
	public String getType() {	return HAPConstantShared.PROCESSSUITE_ELEMENTTYPE_ENTITY;	}

	//steps within task
	public Map<String, HAPDefinitionActivity> getActivities(){  return this.m_activities;  }
	public HAPDefinitionActivity getActivityById(String activityId) {  return this.m_activities.get(activityId);   }
	public void addActivity(String id, HAPDefinitionActivity activity) {  this.m_activities.put(id, activity);    }
	
	public Set<HAPResourceDependency> getRequiredResources(){ return this.m_requiredResources;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ACTIVITY, HAPJsonUtility.buildJson(this.m_activities, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPComponent cloneComponent() {
		return (HAPComponent)this.cloneResourceDefinitionContainerElement();
	}

	@Override
	public HAPElementContainerResourceDefinition cloneResourceDefinitionContainerElement() {
		HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite out = new HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite();
		this.cloneToComponent(out, true);
		for(String name : this.m_activities.keySet()) {
			out.m_activities.put(name, this.m_activities.get(name).cloneActivityDefinition());
		}
		for(HAPResourceDependency dependency : this.m_requiredResources) {
			out.m_requiredResources.add(dependency.cloneResourceDependency());
		}
		return out;
	}
}
