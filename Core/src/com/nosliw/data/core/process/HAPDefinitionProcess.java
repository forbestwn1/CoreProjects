package com.nosliw.data.core.process;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
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
public class HAPDefinitionProcess extends HAPEntityInfoImp{

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String CONTEXT = "context";

	@HAPAttribute
	public static String ACTIVITY = "activity";

	private String m_id;
	
	private List<HAPDefinitionActivity> m_activities;

	//data context, variable definition(absolute, relative), constants
	private HAPContextGroup m_context;

	//dependent resources
	private Set<HAPResourceDependent> m_requiredResources;
	
	public HAPDefinitionProcess(){
		this.m_context = new HAPContextGroup();
		this.m_activities = new ArrayList<HAPDefinitionActivity>();
		this.m_requiredResources = new HashSet<HAPResourceDependent>();
	}

	//steps within task
	public List<HAPDefinitionActivity> getActivities(){  return this.m_activities;  }
	public void addActivity(HAPDefinitionActivity activity) {  this.m_activities.add(activity);    }
	
	public Set<HAPResourceDependent> getRequiredResources(){ return this.m_requiredResources;  }
	
	public String getId() {   return this.m_id;  }
	
	public void setContext(HAPContextGroup context) {   this.m_context = context;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, m_id);
		jsonMap.put(CONTEXT, HAPJsonUtility.buildJson(this.m_context, HAPSerializationFormat.JSON));
		jsonMap.put(ACTIVITY, HAPJsonUtility.buildJson(this.m_activities, HAPSerializationFormat.JSON));
	}
}
