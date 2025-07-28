package com.nosliw.data.core.process1.resource;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.component.HAPDefinitionEntityComponent;
import com.nosliw.data.core.domain.complexentity.HAPElementInContainerEntityDefinition;
import com.nosliw.data.core.domain.complexentity.HAPElementInContainerEntityDefinitionImpComplex;
import com.nosliw.data.core.process1.HAPDefinitionProcess;
import com.nosliw.data.core.resource.HAPResourceDependency;

/**
 * Task is unit that can execute 
 * All information required when describe a task
 * Task is a sequence of steps
 */
@HAPEntityWithAttribute
public class HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite extends HAPElementInContainerEntityDefinitionImpComplex implements HAPDefinitionProcess{ 

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
	public String getElementType() {	return HAPConstantShared.PROCESSSUITE_ELEMENTTYPE_ENTITY;	}

	//steps within task
	public Map<String, HAPDefinitionActivity> getActivities(){  return this.m_activities;  }
	public HAPDefinitionActivity getActivityById(String activityId) {  return this.m_activities.get(activityId);   }
	public void addActivity(String id, HAPDefinitionActivity activity) {  this.m_activities.put(id, activity);    }
	
	public Set<HAPResourceDependency> getRequiredResources(){ return this.m_requiredResources;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ACTIVITY, HAPUtilityJson.buildJson(this.m_activities, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPDefinitionEntityComponent cloneComponent() {
		return (HAPDefinitionEntityComponent)this.cloneDefinitionEntityElementInContainer();
	}

	@Override
	public HAPElementInContainerEntityDefinition cloneDefinitionEntityElementInContainer() {
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
