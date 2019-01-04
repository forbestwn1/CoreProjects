package com.nosliw.data.core.process;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPExecutable;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceData;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.script.context.HAPContext;

@HAPEntityWithAttribute
public class HAPExecutableProcess extends HAPSerializableImp implements HAPExecutable{

	@HAPAttribute
	public static String DEFINITION = "definition";

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String ACTIVITY = "activity";

	@HAPAttribute
	public static String STARTACTIVITYID = "startActivityId";

	@HAPAttribute
	public static String INPUT = "input";

	@HAPAttribute
	public static String RESULT = "result";
	
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
	
	public HAPExecutableProcess(HAPDefinitionProcess definition, String id) {
		this.m_activities = new LinkedHashMap<String, HAPExecutableActivity>();
		this.m_results = new LinkedHashMap<String, HAPDefinitionDataAssociationGroup>();
		this.m_processDefinition = definition;
		this.m_id = id;
	}

	public Map<String, HAPExecutableActivity> getActivities(){  return this.m_activities;   }
	
	public void addActivity(String activityId, HAPExecutableActivity activity) {		this.m_activities.put(activityId, activity);	}
	
	public void setStartActivityId(String id) {   this.m_startActivityId = id;   }

	@Override
	public List<HAPResourceDependent> getResourceDependency(HAPRuntimeInfo runtimeInfo) {		
		//process resources
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		 for(HAPExecutableActivity activity : this.getActivities().values()) {
			 out.addAll(activity.getResourceDependency(runtimeInfo));
		 }
	
		return out;	
	}

	@Override
	public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildBasicJsonMap(jsonMap, typeJsonMap);
		
		Map<String, String> activityJsonMap = new LinkedHashMap<String, String>();
		for(String actId : this.m_activities.keySet()) {
			activityJsonMap.put(actId, this.m_activities.get(actId).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ACTIVITY, HAPJsonUtility.buildMapJson(activityJsonMap));
		
		return HAPResourceDataFactory.createJSValueResourceData(HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap));
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ACTIVITY, HAPJsonUtility.buildJson(this.m_activities, HAPSerializationFormat.JSON));
	}
	
	protected void buildBasicJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		jsonMap.put(ID, this.m_id);
		jsonMap.put(DEFINITION, this.m_processDefinition.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(STARTACTIVITYID, this.m_startActivityId);
		jsonMap.put(INPUT, this.m_input.toStringValue(HAPSerializationFormat.JSON));

		jsonMap.put(RESULT, HAPJsonUtility.buildJson(this.m_results, HAPSerializationFormat.JSON));
	}
}
