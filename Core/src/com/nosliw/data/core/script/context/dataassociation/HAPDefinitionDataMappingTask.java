package com.nosliw.data.core.script.context.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDefinitionDataMappingTask extends HAPSerializableImp{

	@HAPAttribute
	public static String OUTPUTMAPPING = "outputMapping";

	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";

	private HAPDefinitionDataAssociation m_inputMapping;
	
	//data association from process to external
	private Map<String, HAPDefinitionDataAssociation> m_outputMapping;

	public HAPDefinitionDataMappingTask() {
		this.m_outputMapping = new LinkedHashMap<String, HAPDefinitionDataAssociation>();
	}

	public HAPDefinitionDataAssociation getInputMapping() {   return this.m_inputMapping;   }
	public void setInputMapping(HAPDefinitionDataAssociation inputMapping) {    this.m_inputMapping = inputMapping;    }
	public Map<String, HAPDefinitionDataAssociation> getOutputMapping(){    return this.m_outputMapping;     }
	public void setOutputMapping(Map<String, HAPDefinitionDataAssociation> outputMapping) {  if(outputMapping!=null) this.m_outputMapping.putAll(outputMapping);   }
	public void addOutputMapping(String name, HAPDefinitionDataAssociation mapping) {  this.m_outputMapping.put(name, mapping);   }
	
	@Override
	protected boolean buildObjectByJson(Object json){  
		this.buildMapping((JSONObject)json);
		return true;  
	}

	private void buildMapping(JSONObject jsonObj) {
		JSONObject outputMappingJson = jsonObj.optJSONObject(OUTPUTMAPPING);
		if(outputMappingJson!=null) {
			for(Object key : outputMappingJson.keySet()) {
				HAPDefinitionDataAssociation dataAssociation = HAPParserDataAssociation.buildDefinitionByJson(outputMappingJson.optJSONObject((String)key)); 
				this.addOutputMapping((String)key, dataAssociation);
			}
		}
		JSONObject inputMappingJson = jsonObj.optJSONObject(INPUTMAPPING);
		if(inputMappingJson!=null) {
			this.m_inputMapping = HAPParserDataAssociation.buildDefinitionByJson(inputMappingJson); 
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(OUTPUTMAPPING, HAPJsonUtility.buildJson(this.m_outputMapping, HAPSerializationFormat.JSON));
		jsonMap.put(INPUTMAPPING, HAPJsonUtility.buildJson(this.m_inputMapping, HAPSerializationFormat.JSON));
	}

	protected void cloneToTaskDataMappingDefinition(HAPDefinitionDataMappingTask def) {
		if(this.m_inputMapping!=null)  def.m_inputMapping = this.m_inputMapping.cloneDataAssocation();
		for(String name : this.m_outputMapping.keySet()) {
			def.m_outputMapping.put(name, this.m_outputMapping.get(name).cloneDataAssocation());
		}
	}
	
	@Override
	public HAPDefinitionDataMappingTask clone(){
		HAPDefinitionDataMappingTask out = new HAPDefinitionDataMappingTask();
		this.cloneToTaskDataMappingDefinition(out);
		return out;
	}
}
