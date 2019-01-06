package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

public abstract class HAPDefinitionActivityNormal extends HAPDefinitionActivity{

	@HAPAttribute
	public static String INPUT = "input";

	@HAPAttribute
	public static String RESULT = "result";

	//associate variable in process to input required by activity 
	private HAPDefinitionDataAssociationGroup m_input;
	
	//possible result for activity
	private Map<String, HAPDefinitionResultActivityNormal> m_results;
	
	public HAPDefinitionActivityNormal() {
		this.m_results = new LinkedHashMap<String, HAPDefinitionResultActivityNormal>();
		this.m_input = new HAPDefinitionDataAssociationGroup();
	}
	
	public HAPDefinitionDataAssociationGroup getInput() {  return this.m_input;   }
	
	public Map<String, HAPDefinitionResultActivityNormal> getResults(){   return this.m_results;  }
	public HAPDefinitionResultActivityNormal getResult(String resultName){   return this.m_results.get(resultName);  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			
			JSONObject inputJson = jsonObj.optJSONObject(INPUT);
			if(inputJson!=null) {
				this.m_input = new HAPDefinitionDataAssociationGroup();
				this.m_input.buildObject(inputJson, HAPSerializationFormat.JSON);
			}
			
			JSONArray resultsJson = jsonObj.optJSONArray(RESULT);
			if(resultsJson!=null) {
				for(int i=0; i<resultsJson.length(); i++) {
					HAPDefinitionResultActivityNormal result = new HAPDefinitionResultActivityNormal();
					result.buildObject(resultsJson.get(i), HAPSerializationFormat.JSON);
					this.m_results.put(result.getName(), result);
				}
			}
			return true;  
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_input!=null)		jsonMap.put(INPUT, this.m_input.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPJsonUtility.buildJson(this.m_results, HAPSerializationFormat.JSON));
	}
}
