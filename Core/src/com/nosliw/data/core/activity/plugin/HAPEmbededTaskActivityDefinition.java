package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;
import com.nosliw.data.core.component.HAPDefinitionEntityComplex;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataMappingTask;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.task.HAPDefinitionTask;

public class HAPEmbededTaskActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String TASK = "task";
	
	@HAPAttribute
	public static String DATAMAPPING = "dataMapping";

	private Object m_task;
	
	private HAPDefinitionDataMappingTask m_dataMapping;

	public HAPEmbededTaskActivityDefinition(String type) {
		super(type);
	}
	
	public HAPResourceId getProcess(){  return this.m_task;    }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_task = HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS, jsonObj.opt(TASK));
		this.m_dataMapping = new HAPDefinitionDataMappingTask();
		this.m_dataMapping.buildObject(jsonObj.optJSONObject(DATAMAPPING), HAPSerializationFormat.JSON);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TASK, HAPJsonUtility.buildJson(this.m_task, HAPSerializationFormat.JSON));
		jsonMap.put(DATAMAPPING, HAPJsonUtility.buildJson(this.m_dataMapping, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPEmbededTaskActivityDefinition out = new HAPEmbededTaskActivityDefinition(this.getActivityType());
		this.cloneToTaskActivityDefinition(out);
		out.m_task = this.m_task.clone();
		return out;
	}

	@Override
	public HAPDefinitionTask cloneTaskDefinition() {  return this.cloneActivityDefinition();  }

	@Override
	public void parseActivityDefinition(Object obj, HAPDefinitionEntityComplex complexEntity,
			HAPSerializationFormat format) {
		// TODO Auto-generated method stub
		
	}
}
