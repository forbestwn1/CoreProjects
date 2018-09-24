package com.nosliw.data.core.flow;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.info.HAPInfoImpSimple;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextParser;

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

	@HAPAttribute
	public static String RESULT = "result";
	
	private String m_name;
	
	private String m_description;
	
	private List<HAPDefinitionStep> m_steps;

	//data context, variable definition(absolute, relative), constants
	private HAPContextGroup m_dataContext;

	//result of task
	//result have different type of result(success, failure, ...)
	//each result type response to one root node
	private HAPContext m_result;
	
	private HAPInfo m_info;
	
	//dependent resources
	private Set<HAPResourceDependent> m_requiredResources;

	public HAPDefinitionTask(){
		this.m_steps = new ArrayList<HAPDefinitionStep>();
		this.m_requiredResources = new HashSet<HAPResourceDependent>();
		this.m_result = new HAPContext();
	}

	public String getName() {  return this.m_name;  }
	
	//steps within task
	public List<HAPDefinitionStep> getSteps(){  return this.m_steps;  }
	
	//related information, for instance, description, 
	public HAPInfo getInfo(){  return this.m_info;  }
	
	public HAPContext getResult() {  return this.m_result;   }
	
	public Set<HAPResourceDependent> getRequiredResources(){ return this.m_requiredResources;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		
		JSONObject infoObj = jsonObj.optJSONObject(INFO);
		if(infoObj!=null){
			this.m_info = new HAPInfoImpSimple();
			this.m_info.buildObject(infoObj, HAPSerializationFormat.JSON);
		}
		
		JSONObject resultObj = jsonObj.optJSONObject(RESULT);
		if(resultObj!=null) 	HAPContextParser.parseContext(resultObj, m_result);
		
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPJsonUtility.buildJson(this.m_result, HAPSerializationFormat.JSON));
	}
}
