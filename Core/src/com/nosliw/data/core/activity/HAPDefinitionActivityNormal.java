package com.nosliw.data.core.activity;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.complex.valuestructure.HAPWrapperValueStructure;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.task.HAPDefinitionTask;

public abstract class HAPDefinitionActivityNormal extends HAPDefinitionActivity{

	@HAPAttribute
	public static String INPUT = "input";

	@HAPAttribute
	public static String RESULT = "result";

	//associate variable in process to input required by activity 
	private HAPDefinitionDataAssociation m_inputDataAssociation;
	
	//possible result for activity
	private Map<String, HAPDefinitionResultActivity> m_results;
	
	public HAPDefinitionActivityNormal(String type) {
		super(type);
		this.m_results = new LinkedHashMap<String, HAPDefinitionResultActivity>();
	}
	
	public HAPDefinitionDataAssociation getInputDataAssociation() {  return this.m_inputDataAssociation;   }
	public void setInputDataAssociation(HAPDefinitionDataAssociation input) {   this.m_inputDataAssociation = input;   }
	
	//get input context structure for activity
	//it is for process input mapping
	//param: parent context structure
	public HAPWrapperValueStructure getInputValueStructureWrapper() {  return null;   }
	
	public void addResult(String name, HAPDefinitionResultActivity result) {   this.m_results.put(name, result);   }
	public Map<String, HAPDefinitionResultActivity> getResults(){   return this.m_results;  }
	public HAPDefinitionResultActivity getResult(String resultName){   return this.m_results.get(resultName);  }
	
	//if no inputmapping, build default one which is mirror
	protected void buildDefaultInputMapping() {
		if(this.getInputDataAssociation()==null)	this.setInputDataAssociation(new HAPDefinitionDataAssociationMirror());
	}

	protected void buildDefaultResultOutputMapping() {
		Map<String, HAPDefinitionResultActivity> results = this.getResults();
		for(String resultName : results.keySet()) {
			if(results.get(resultName).getOutputDataAssociation()==null) {
				results.get(resultName).setOutputDataAssociation(new HAPDefinitionDataAssociationMirror());
			}
		}
	}
	
	abstract protected void buildConfigureByJson(JSONObject configurJsonObj);
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			
			JSONObject configureObj = this.getConfigurationObject(jsonObj);
			buildConfigureByJson(configureObj);
			
			JSONObject inputJson = jsonObj.optJSONObject(INPUT);
			if(inputJson!=null) {
				this.m_inputDataAssociation = HAPParserDataAssociation.buildDefinitionByJson(inputJson); 
			}
			
			JSONArray resultsJson = jsonObj.optJSONArray(RESULT);
			if(resultsJson!=null) {
				for(int i=0; i<resultsJson.length(); i++) {
					HAPDefinitionResultActivity result = new HAPDefinitionResultActivity();
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
		if(this.m_inputDataAssociation!=null)		jsonMap.put(INPUT, this.m_inputDataAssociation.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPJsonUtility.buildJson(this.m_results, HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPDefinitionTask cloneTaskDefinition() {  return this.cloneActivityDefinition();  }

	protected void cloneToNormalActivityDefinition(HAPDefinitionActivityNormal activity) {
		this.cloneToActivityDefinition(activity);
		if(this.m_inputDataAssociation!=null)  activity.m_inputDataAssociation = this.m_inputDataAssociation.cloneDataAssocation();
		for(String name : this.m_results.keySet()) {
			activity.m_results.put(name, this.m_results.get(name).cloneNormalActivityResultDefinition());
		}
	}
}
