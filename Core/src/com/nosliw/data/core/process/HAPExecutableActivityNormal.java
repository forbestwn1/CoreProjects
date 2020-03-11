package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPParserDataAssociation;

public abstract class HAPExecutableActivityNormal extends HAPExecutableActivity{

	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";
	
	@HAPAttribute
	public static String RESULT = "result";

	private HAPExecutableDataAssociation m_inputMapping;

	private Map<String, HAPExecutableResultActivityNormal> m_results;

	public HAPExecutableActivityNormal() {}

	public HAPExecutableActivityNormal(String id, HAPDefinitionActivityNormal activityDef) {
		super(HAPConstant.ACTIVITY_CATEGARY_NORMAL, id, activityDef);
		this.m_results = new LinkedHashMap<String, HAPExecutableResultActivityNormal>();
	}
	
	public void setInputDataAssociation(HAPExecutableDataAssociation input) {  this.m_inputMapping = input;  }
	public HAPExecutableDataAssociation getInputDataAssociation() {   return this.m_inputMapping;   }
	 
//	public HAPDefinitionActivityNormal getNormalActivityDefinition() {   return (HAPDefinitionActivityNormal)this.getActivityDefinition();  }
	
	public Map<String, HAPExecutableResultActivityNormal> getResults(){   return this.m_results;   }
	public HAPExecutableResultActivityNormal getResult(String name) {   return this.m_results.get(name);   }

	public void addResult(String name, HAPExecutableResultActivityNormal result) {   this.m_results.put(name, result);   }
	
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
		jsonMap.put(RESULT, HAPJsonUtility.buildJson(this.m_results, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(this.m_inputMapping!=null)  jsonMap.put(INPUTMAPPING, this.m_inputMapping.toResourceData(runtimeInfo).toString());
		
		Map<String, String> resultJsonMap = new LinkedHashMap<String, String>();
		for(String resultName : this.m_results.keySet()) {
			resultJsonMap.put(resultName, this.m_results.get(resultName).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(RESULT, HAPJsonUtility.buildMapJson(resultJsonMap));
	}	
}
