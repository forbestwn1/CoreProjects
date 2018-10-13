package com.nosliw.data.core.task;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.script.context.HAPContextGroup;

/**
 * Task is unit that can execute 
 * All information required when describe a task
 * Task is a sequence of steps
 */
@HAPEntityWithAttribute
public class HAPDefinitionTask extends HAPSerializableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DESCRIPTION = "description";

	@HAPAttribute
	public static String DATACONTEXT = "context";

	@HAPAttribute
	public static String STEP = "step";

	@HAPAttribute
	public static String INFO = "info";

	private String m_name;
	
	private String m_description;
	
	private List<HAPDefinitionStep> m_steps;

	//data context, variable definition(absolute, relative), constants
	private HAPContextGroup m_context;

	private HAPInfo m_info;
	
	//dependent resources
	private Set<HAPResourceDependent> m_requiredResources;
	
	private HAPManagerTask m_taskManager;

	public HAPDefinitionTask(HAPManagerTask taskManager){
		this.m_taskManager = taskManager;
		this.m_context = new HAPContextGroup();
		this.m_steps = new ArrayList<HAPDefinitionStep>();
		this.m_requiredResources = new HashSet<HAPResourceDependent>();
	}

	public String getName() {  return this.m_name;  }
	
	//steps within task
	public List<HAPDefinitionStep> getSteps(){  return this.m_steps;  }
	
	//related information, for instance, description, 
	public HAPInfo getInfo(){  return this.m_info;  }
	
	public Set<HAPResourceDependent> getRequiredResources(){ return this.m_requiredResources;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		
		this.m_name = jsonObj.optString(NAME);
		this.m_description = jsonObj.optString(DESCRIPTION);
		
		this.m_context.buildObject(jsonObj.optJSONObject(DATACONTEXT), HAPSerializationFormat.JSON);
		
		JSONArray stepArrayJson = jsonObj.optJSONArray(STEP);
		for(int i=0; i<stepArrayJson.length(); i++) {
			JSONObject stepObjJson = (JSONObject)stepArrayJson.get(i);
			String stepType = stepObjJson.getString(HAPDefinitionStep.TYPE);
			HAPDefinitionStep stepDef = this.m_taskManager.getStepPlug(stepType).buildStepDefinition(stepObjJson);
			this.m_steps.add(stepDef);
		}
		
		JSONObject infoObj = jsonObj.optJSONObject(INFO);
		if(infoObj!=null){
			this.m_info = new HAPInfoImpSimple();
			this.m_info.buildObject(infoObj, HAPSerializationFormat.JSON);
		}
		
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, m_name);
		jsonMap.put(DESCRIPTION, m_description);
		jsonMap.put(DATACONTEXT, HAPJsonUtility.buildJson(this.m_context, HAPSerializationFormat.JSON));
		jsonMap.put(STEP, HAPJsonUtility.buildJson(this.m_steps, HAPSerializationFormat.JSON));
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}
}
