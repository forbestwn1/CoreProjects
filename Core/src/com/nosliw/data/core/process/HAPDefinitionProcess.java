package com.nosliw.data.core.process;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPChildrenComponentIdContainer;
import com.nosliw.data.core.component.HAPComponentImp;
import com.nosliw.data.core.resource.HAPResourceDependency;

/**
 * Task is unit that can execute 
 * All information required when describe a task
 * Task is a sequence of steps
 */
@HAPEntityWithAttribute
public class HAPDefinitionProcess extends HAPComponentImp{ 

	@HAPAttribute
	public static String ACTIVITY = "activity";

	private Map<String, HAPDefinitionActivity> m_activities;

	//dependent resources
	private Set<HAPResourceDependency> m_requiredResources;

	public HAPDefinitionProcess(){
		this.m_activities = new LinkedHashMap<String, HAPDefinitionActivity>();
		this.m_requiredResources = new HashSet<HAPResourceDependency>();
	}
 
	//steps within task
	public Map<String, HAPDefinitionActivity> getActivities(){  return this.m_activities;  }
	public HAPDefinitionActivity getActivityById(String activityId) {  return this.m_activities.get(activityId);   }
	public void addActivity(String id, HAPDefinitionActivity activity) {  this.m_activities.put(id, activity);    }
	
	public Set<HAPResourceDependency> getRequiredResources(){ return this.m_requiredResources;  }
	
	@Override
	public HAPChildrenComponentIdContainer getChildrenComponentId() {
		return null;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ACTIVITY, HAPJsonUtility.buildJson(this.m_activities, HAPSerializationFormat.JSON));
	}
}
