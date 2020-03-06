package com.nosliw.data.core.script.context.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDefinitionWrapperTask<T> extends HAPSerializableImp{

	@HAPAttribute
	public static String OUTPUTMAPPING = "outputMapping";

	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";

	@HAPAttribute
	public static String TASK = "task";
	
	private HAPDefinitionDataAssociation m_inputMapping;
	
	//data association from process to external
	private Map<String, HAPDefinitionDataAssociation> m_outputMapping;

	private T m_taskDefinition;

	public HAPDefinitionWrapperTask() {
		this.m_outputMapping = new LinkedHashMap<String, HAPDefinitionDataAssociation>();
	}

	public HAPDefinitionWrapperTask(T taskDef) {
		this();
		this.m_taskDefinition = taskDef;
	}

	public T getTaskDefinition() {  return this.m_taskDefinition;   }
	public void setTaskDefinition(T taskDef) {    this.m_taskDefinition = taskDef;     }
	
	public HAPDefinitionDataAssociation getInputMapping() {   return this.m_inputMapping;   }
	public void setInputMapping(HAPDefinitionDataAssociation inputMapping) {    this.m_inputMapping = inputMapping;    }
	public Map<String, HAPDefinitionDataAssociation> getOutputMapping(){    return this.m_outputMapping;     }
	public void setOutputMapping(Map<String, HAPDefinitionDataAssociation> outputMapping) {  if(outputMapping!=null) this.m_outputMapping.putAll(outputMapping);   }
	public void addOutputMapping(String name, HAPDefinitionDataAssociation mapping) {  this.m_outputMapping.put(name, mapping);   }
	
	public void buildMapping(JSONObject jsonObj) {
		JSONObject outputMappingJson = jsonObj.optJSONObject(OUTPUTMAPPING);
		if(outputMappingJson!=null) {
			for(Object key : outputMappingJson.keySet()) {
				HAPDefinitionDataAssociation dataAssociation = HAPParserDataAssociation.buildObjectByJson(outputMappingJson.optJSONObject((String)key)); 
				this.addOutputMapping((String)key, dataAssociation);
			}
		}
		JSONObject inputMappingJson = jsonObj.optJSONObject(INPUTMAPPING);
		if(inputMappingJson!=null) {
			this.m_inputMapping = HAPParserDataAssociation.buildObjectByJson(inputMappingJson); 
		}
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TASK, HAPJsonUtility.buildJson(this.m_taskDefinition, HAPSerializationFormat.JSON));
		jsonMap.put(OUTPUTMAPPING, HAPJsonUtility.buildJson(this.m_outputMapping, HAPSerializationFormat.JSON));
		jsonMap.put(INPUTMAPPING, HAPJsonUtility.buildJson(this.m_inputMapping, HAPSerializationFormat.JSON));
	}
	
	@Override
	public HAPDefinitionWrapperTask<T> clone(){
		HAPDefinitionWrapperTask<T> out = new HAPDefinitionWrapperTask<T>();
		if(this.m_inputMapping!=null)  out.m_inputMapping = this.m_inputMapping.cloneDataAssocation();
		for(String name : this.m_outputMapping.keySet()) {
			out.m_outputMapping.put(name, this.m_outputMapping.get(name).cloneDataAssocation());
		}
		out.m_taskDefinition = this.m_taskDefinition;
		return out;
	}
}
