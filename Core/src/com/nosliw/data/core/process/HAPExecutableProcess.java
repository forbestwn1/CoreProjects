package com.nosliw.data.core.process;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPJsonTypeScript;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.resource.HAPResourceDefinitionProcess;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.script.context.HAPUtilityContextScript;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableTask;
import com.nosliw.data.core.script.context.dataassociation.HAPParserDataAssociation;

@HAPEntityWithAttribute
public class HAPExecutableProcess extends HAPExecutableImp implements HAPExecutableTask{

	@HAPAttribute
	public static String DEFINITION = "definition";

	@HAPAttribute
	public static String ID = "id";

	@HAPAttribute
	public static String ACTIVITY = "activity";

	@HAPAttribute
	public static String STARTACTIVITYID = "startActivityId";

	@HAPAttribute
	public static String CONTEXT = "context";

	@HAPAttribute
	public static String RESULT = "result";

	@HAPAttribute
	public static String INITSCRIPT = "initScript";

	//process definition
	private HAPResourceDefinitionProcess m_processDefinition;
	
	//unique in system
	private String m_id;
	
	//all activity
	private Map<String, HAPExecutableActivity> m_activities;
	
	//activity to start with in process
	private String m_startActivityId;
	
	//input variables
	private HAPContextGroup m_context;  
	
	//all possible result
	private Map<String, HAPExecutableDataAssociation> m_results;

	private HAPManagerActivityPlugin m_activityPluginMan;
	
	public HAPExecutableProcess(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}
	
	public HAPExecutableProcess(HAPResourceDefinitionProcess definition, String id, HAPManagerActivityPlugin activityPluginMan) {
		this(activityPluginMan);
		this.m_activities = new LinkedHashMap<String, HAPExecutableActivity>();
		this.m_results = new LinkedHashMap<String, HAPExecutableDataAssociation>();
		this.m_processDefinition = definition;
		this.m_id = id;
	}
 
	@Override
	public HAPParentContext getInContext() {	return HAPParentContext.createDefault(this.m_context.getChildContext(HAPConstantShared.UIRESOURCE_CONTEXTTYPE_PUBLIC));	}

	@Override
	public Map<String, HAPParentContext> getOutResultContext() {
		Map<String, HAPParentContext> out = new LinkedHashMap<String, HAPParentContext>();
		for(String resultName : this.m_results.keySet()) {
//			out.put(resultName, HAPParentContext.createDefault(this.m_context));     //kkkkkk
			out.put(resultName, HAPParentContext.createDefault(this.m_results.get(resultName).getOutput().getOutputStructure()));
		}
		String defaultResultName = HAPConstantShared.NAME_DEFAULT;
		if(out.get(defaultResultName)==null) {
			out.put(defaultResultName, HAPParentContext.createDefault(this.m_context));
		}
		return out;
	}

	public HAPResourceDefinitionProcess getDefinition() {   return this.m_processDefinition;    }
	
	public Map<String, HAPExecutableActivity> getActivities(){  return this.m_activities;   }
	
	public void addActivity(String activityId, HAPExecutableActivity activity) {		this.m_activities.put(activityId, activity);	}
	
	public void setStartActivityId(String id) {   this.m_startActivityId = id;   }

	public void setResults(Map<String, HAPExecutableDataAssociation> results) {   this.m_results.putAll(results);   }
	public HAPExecutableDataAssociation getResult(String result) {  return this.m_results.get(result);   }
	public Set<String> getResultNames(){   return this.m_results.keySet();  }
	
	public HAPContextGroup getContext() {   return this.m_context;  }
	public void setContext(HAPContextGroup context) {   this.m_context = context;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(json);
		
		this.m_id = jsonObj.getString(ID);
		
		this.m_startActivityId = jsonObj.getString(STARTACTIVITYID);
		
		this.m_context = HAPParserContext.parseContextGroup(jsonObj.getJSONObject(CONTEXT));
		
		JSONObject dsJsonObj = jsonObj.getJSONObject(RESULT);
		for(Object key : dsJsonObj.keySet()) {
			this.m_results.put((String)key, HAPParserDataAssociation.buildExecutalbeByJson(dsJsonObj.getJSONObject((String)key))); 
		}
		
		JSONObject activitysJsonObj = jsonObj.getJSONObject(ACTIVITY);
		for(Object key : activitysJsonObj.keySet()) {
			JSONObject activityJsonObj = activitysJsonObj.getJSONObject((String)key);
			String pluginType = activityJsonObj.getString(HAPExecutableActivity.TYPE);
			HAPExecutableActivity activityExe = this.m_activityPluginMan.getPlugin(pluginType).buildActivityExecutable(activityJsonObj);
			this.m_activities.put((String)key, activityExe);
		}
		
		return true;  
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {		
		//process resources
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		 for(HAPExecutableActivity activity : this.getActivities().values()) {
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
		
		Map<String, String> resultsJsonMap = new LinkedHashMap<String, String>();
		for(String resultName : this.m_results.keySet()) {
			resultsJsonMap.put(resultName, this.m_results.get(resultName).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(RESULT, HAPJsonUtility.buildMapJson(resultsJsonMap));
	
		jsonMap.put(INITSCRIPT, HAPUtilityContextScript.buildContextInitScript(this.getContext()).getScript());
		typeJsonMap.put(INITSCRIPT, HAPJsonTypeScript.class);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
//		jsonMap.put(DEFINITION, this.m_processDefinition.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(STARTACTIVITYID, this.m_startActivityId);
		jsonMap.put(CONTEXT, this.m_context.toStringValue(HAPSerializationFormat.JSON));

		jsonMap.put(RESULT, HAPJsonUtility.buildJson(this.m_results, HAPSerializationFormat.JSON));
		jsonMap.put(ACTIVITY, HAPJsonUtility.buildJson(this.m_activities, HAPSerializationFormat.JSON));
	}
}
