package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.HAPContextFlat;

public abstract class HAPExecutableActivityNormal extends HAPExecutableActivity{

	@HAPAttribute
	public static String INPUT = "input";
	
	@HAPAttribute
	public static String RESULT = "result";

	private HAPExecutableDataAssociationGroup m_input;

	private Map<String, HAPExecutableResultActivityNormal> m_results;
	
	public HAPExecutableActivityNormal(String id, HAPDefinitionActivity activityDef) {
		super(id, activityDef);
		this.m_results = new LinkedHashMap<String, HAPExecutableResultActivityNormal>();
	}

	public void setInputDataAssociation(HAPExecutableDataAssociationGroup input) {  this.m_input = input;  }

	public HAPContextFlat getInputContext() {
		if(this.m_input==null)   return null;
		return this.m_input.getContext();   
	}
	
	public Map<String, HAPExecutableResultActivityNormal> getResults(){   return this.m_results;   }
	public HAPExecutableResultActivityNormal getResult(String name) {   return this.m_results.get(name);   }

	public void addResult(String name, HAPExecutableResultActivityNormal result) {   this.m_results.put(name, result);   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_input!=null)		jsonMap.put(INPUT, this.m_input.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPJsonUtility.buildJson(this.m_results, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		if(this.m_input!=null)  jsonMap.put(INPUT, this.m_input.toResourceData(runtimeInfo).toString());
		
		Map<String, String> resultJsonMap = new LinkedHashMap<String, String>();
		for(String resultName : this.m_results.keySet()) {
			resultJsonMap.put(resultName, this.m_results.get(resultName).toResourceData(runtimeInfo).toString());
		}
		jsonMap.put(RESULT, HAPJsonUtility.buildMapJson(resultJsonMap));
	}	
}
