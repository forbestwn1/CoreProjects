package com.nosliw.data.core.activity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.process1.HAPExecutableActivity;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.valuestructure.HAPWrapperValueStructure;

@HAPEntityWithAttribute
public class HAPExecutableActivitySuite extends HAPExecutableImp implements HAPWithEntityElement<HAPExecutableActivity>{

	@HAPAttribute
	public static String DEFINITION = "definition";

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String ACTIVITY = "activity";

	@HAPAttribute
	public static String VALUESTRUCTURE = "context";

	private String m_id;
	
	//process definition
	private HAPWrapperValueStructure m_valueStructureWrapper;

	private HAPDefinitionActivitySuite m_definition;
	
	private Map<String, HAPExecutableActivity> m_activities;
	
	public HAPExecutableActivitySuite(HAPDefinitionActivitySuite definition, String id){
		this.m_id = id;
		this.m_definition = definition;
		this.setValueStructureDefinitionWrapper(definition.getValueStructureWrapper());
	}
	
	public HAPDefinitionActivitySuite getDefinition() {    return this.m_definition;	}

	public void setValueStructureDefinitionWrapper(HAPWrapperValueStructure valueStructureWrapper) {   	this.m_valueStructureWrapper = valueStructureWrapper;	}
	
	public HAPWrapperValueStructure getValueStructureDefinitionWrapper() {    return this.m_valueStructureWrapper;    }

	@Override
	public Set<HAPExecutableActivity> getEntityElements() {  return new HashSet<HAPExecutableActivity>(this.m_activities.values());  }

	@Override
	public HAPExecutableActivity getEntityElement(String id) {   return this.m_activities.get(id);  }

	@Override
	public void addEntityElement(HAPExecutableActivity entityElement) {   this.m_activities.put(entityElement.getId(), entityElement);  }

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {		
		//process resources
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		 for(HAPExecutableActivity activity : this.m_activities.values()) {
			 out.addAll(activity.getResourceDependency(runtimeInfo, resourceManager));
		 }
		return out;	
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		Map<String, String> activityJsonMap = new LinkedHashMap<String, String>();
		for(String actId : this.m_activities.keySet()) {
			activityJsonMap.put(actId, this.m_activities.get(actId).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(ACTIVITY, HAPJsonUtility.buildMapJson(activityJsonMap));
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
		jsonMap.put(ACTIVITY, HAPJsonUtility.buildJson(this.m_activities, HAPSerializationFormat.JSON));
	}

}
