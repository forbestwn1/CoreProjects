package com.nosliw.data.core.dataassociation;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPDefinitionWrapperTask<T> extends HAPDefinitionGroupDataAssociationForTask{

	@HAPAttribute
	public static String TASK = "task";
	
	private T m_taskDefinition;

	public HAPDefinitionWrapperTask() {
	}

	public HAPDefinitionWrapperTask(T taskDef) {
		this.m_taskDefinition = taskDef;
	}

	public T getTaskDefinition() {  return this.m_taskDefinition;   }
	public void setTaskDefinition(T taskDef) {    this.m_taskDefinition = taskDef;     }
	
	public void buildObj(JSONObject jsonObj, T taskObj) {
		super.buildObjectByJson(jsonObj);
		if(taskObj!=null)	this.m_taskDefinition = taskObj;
	}
	
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, String taskAttName) {
		jsonMap.put(taskAttName, HAPJsonUtility.buildJson(this.m_taskDefinition, HAPSerializationFormat.JSON));
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	public HAPDefinitionWrapperTask<T> clone(){
		HAPDefinitionWrapperTask<T> out = new HAPDefinitionWrapperTask<T>();
		this.cloneToTaskDataMappingDefinition(out);
		out.m_taskDefinition = this.m_taskDefinition;
		return out;
	}
}
