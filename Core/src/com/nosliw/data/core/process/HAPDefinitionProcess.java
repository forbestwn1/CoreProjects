package com.nosliw.data.core.process;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.script.context.HAPContextGroup;

/**
 * Task is unit that can execute 
 * All information required when describe a task
 * Task is a sequence of steps
 */
@HAPEntityWithAttribute
public class HAPDefinitionProcess extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String CONTEXT = "context";

	@HAPAttribute
	public static String ACTIVITY = "activity";

	//data context, variable definition(absolute, relative), constants
	private HAPContextGroup m_context;

	private Map<String, HAPDefinitionActivity> m_activities;

	//dependent resources
	private Set<HAPResourceDependent> m_requiredResources;
	
	public HAPDefinitionProcess(){
		this.m_context = new HAPContextGroup();
		this.m_activities = new LinkedHashMap<String, HAPDefinitionActivity>();
		this.m_requiredResources = new HashSet<HAPResourceDependent>();
	}

	//steps within task
	public Map<String, HAPDefinitionActivity> getActivities(){  return this.m_activities;  }
	public HAPDefinitionActivity getActivityById(String activityId) {  return this.m_activities.get(activityId);   }
	public void addActivity(String id, HAPDefinitionActivity activity) {  this.m_activities.put(id, activity);    }
	
	public Set<HAPResourceDependent> getRequiredResources(){ return this.m_requiredResources;  }
	
	public HAPContextGroup getContext() {   return this.m_context;   }
	public void setContext(HAPContextGroup context) { 
		if(context==null)    this.m_context = new HAPContextGroup();
		else		this.m_context = context;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_context, HAPSerializationFormat.JSON));
		jsonMap.put(ACTIVITY, HAPJsonUtility.buildJson(this.m_activities, HAPSerializationFormat.JSON));
	}
}
