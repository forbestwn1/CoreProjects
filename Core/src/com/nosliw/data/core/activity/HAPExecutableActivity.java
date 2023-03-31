package com.nosliw.data.core.activity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.domain.entity.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.process1.HAPExecutableResultActivityNormal;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.task.HAPExecutableTask;

@HAPEntityWithAttribute
public abstract class HAPExecutableActivity extends HAPExecutableTask{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";
	
	@HAPAttribute
	public static String RESULT = "result";

	private String m_type;
	
	private HAPExecutableDataAssociation m_inputMapping;

	private Map<String, HAPExecutableResultActivity> m_results;

	public HAPExecutableActivity(String type) {
		super(HAPConstantShared.TASK_TYPE_ACTIVITY);
		this.m_type = type;
	}

	public HAPExecutableActivity(String type, String id, HAPDefinitionActivityNormal activityDef) {
		this(type);
		this.setId(id);
		this.m_results = new LinkedHashMap<String, HAPExecutableResultActivity>();
	}
	
	public void setInputDataAssociation(HAPExecutableDataAssociation input) {  this.m_inputMapping = input;  }
	public HAPExecutableDataAssociation getInputDataAssociation() {   return this.m_inputMapping;   }
	 
	public Map<String, HAPExecutableResultActivity> getResults(){   return this.m_results;   }
	public HAPExecutableResultActivity getResult(String name) {   return this.m_results.get(name);   }

	public void addResult(String name, HAPExecutableResultActivity result) {   this.m_results.put(name, result);   }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_inputMapping = HAPParserDataAssociation.buildExecutalbeByJson(jsonObj.getJSONObject(INPUTMAPPING));
		JSONObject resultsJsonObj = jsonObj.getJSONObject(RESULT);
		for(Object key : resultsJsonObj.keySet()) {
			HAPExecutableResultActivityNormal result = new HAPExecutableResultActivityNormal();
			result.buildObject(resultsJsonObj.getJSONObject((String)key), HAPSerializationFormat.JSON);
		}
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_inputMapping!=null)		jsonMap.put(INPUTMAPPING, this.m_inputMapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPUtilityJson.buildJson(this.m_results, HAPSerializationFormat.JSON));
		jsonMap.put(TYPE, this.m_type);
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(this.m_inputMapping!=null)  jsonMap.put(INPUTMAPPING, this.m_inputMapping.toResourceData(runtimeInfo).toString());
		
		Map<String, String> resultJsonMap = new LinkedHashMap<String, String>();
		for(String resultName : this.m_results.keySet()) {
			resultJsonMap.put(resultName, this.m_results.get(resultName).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(RESULT, HAPUtilityJson.buildMapJson(resultJsonMap));
	}	
}
