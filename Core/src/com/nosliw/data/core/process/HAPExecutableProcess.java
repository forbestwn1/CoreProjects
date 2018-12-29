package com.nosliw.data.core.process;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.script.context.HAPContext;

@HAPEntityWithAttribute
public class HAPExecutableProcess extends HAPSerializableImp implements HAPExecutable{

	//process definition
	private HAPDefinitionProcess m_processDefinition;
	
	//unique in system
	private String m_id;
	
	//all activity
	private Map<String, HAPExecutableActivity> m_activities;
	
	//activity to start with in process
	private String m_startActivityId;
	
	//input variables
	private HAPContext m_input;  
	
	//all possible result
	private Map<String, HAPDefinitionDataAssociationGroup> m_results;
	
	private List<HAPResourceDependent> m_resources;
	
	public HAPExecutableProcess(HAPDefinitionProcess definition, String id) {
		this.m_activities = new LinkedHashMap<String, HAPExecutableActivity>();
		this.m_results = new LinkedHashMap<String, HAPDefinitionDataAssociationGroup>();
		this.m_resources = new ArrayList<HAPResourceDependent>();
		this.m_processDefinition = definition;
		this.m_id = id;
	}

	public Map<String, HAPExecutableActivity> getActivities(){  return this.m_activities;   }
	
	public void addActivity(String activityId, HAPExecutableActivity activity) {		this.m_activities.put(activityId, activity);	}
	
	public void setStartActivityId(String id) {   this.m_startActivityId = id;   }

	@Override
	public List<HAPResourceDependent> getResourceDependency() {		return this.m_resources;	}
	
	public void addResoruceDependency(List<HAPResourceDependent> resources) {   this.m_resources.addAll(resources);  }
	
}
