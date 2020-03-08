package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPContextStructureEmpty;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPDefinitionDataAssociationMirror;

public abstract class HAPDefinitionActivityNormal extends HAPDefinitionActivity{

	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";

	@HAPAttribute
	public static String RESULT = "result";

	//associate variable in process to input required by activity 
	private HAPDefinitionDataAssociation m_inputMapping;
	
	//possible result for activity
	private Map<String, HAPDefinitionResultActivityNormal> m_results;
	
	public HAPDefinitionActivityNormal(String type) {
		super(type);
		this.m_results = new LinkedHashMap<String, HAPDefinitionResultActivityNormal>();
	}
	
	public HAPDefinitionDataAssociation getInputMapping() {  return this.m_inputMapping;   }
	public void setInputMapping(HAPDefinitionDataAssociation input) {   this.m_inputMapping = input;   }
	
	//get input context structure for activity
	//it is for process input mapping
	//param: parent context structure
	public HAPContextStructure getInputContextStructure(HAPContextStructure parentContextStructure) {  return HAPContextStructureEmpty.flatStructure();   }
	
	public Map<String, HAPDefinitionResultActivityNormal> getResults(){   return this.m_results;  }
	public HAPDefinitionResultActivityNormal getResult(String resultName){   return this.m_results.get(resultName);  }
	
	//if no inputmapping, build default one which is mirror
	protected void buildDefaultInputMapping() {
		if(this.getInputMapping()==null)	this.setInputMapping(new HAPDefinitionDataAssociationMirror());
	}

	protected void buildDefaultResultOutputMapping() {
		Map<String, HAPDefinitionResultActivityNormal> results = this.getResults();
		for(String resultName : results.keySet()) {
			if(results.get(resultName).getOutputDataAssociation()==null) {
				results.get(resultName).setOutputDataAssociation(new HAPDefinitionDataAssociationMirror());
			}
		}
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			
			JSONObject inputJson = jsonObj.optJSONObject(INPUTMAPPING);
			if(inputJson!=null) {
				this.m_inputMapping = HAPParserDataAssociation.buildDefinitionByJson(inputJson); 
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
		if(this.m_inputMapping!=null)		jsonMap.put(INPUTMAPPING, this.m_inputMapping.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(RESULT, HAPJsonUtility.buildJson(this.m_results, HAPSerializationFormat.JSON));
	}
	
	protected void cloneToNormalActivityDefinition(HAPDefinitionActivityNormal activity) {
		this.cloneToActivityDefinition(activity);
		if(this.m_inputMapping!=null)  activity.m_inputMapping = this.m_inputMapping.cloneDataAssocation();
		for(String name : this.m_results.keySet()) {
			activity.m_results.put(name, this.m_results.get(name).cloneNormalActivityResultDefinition());
		}
	}
}
