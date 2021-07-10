package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPEmbededTaskActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String TASK = "task";
	
	private Object m_task;
	
	public HAPEmbededTaskActivityDefinition(String type) {
		super(type);
	}
	
	public HAPResourceId getProcess(){  return this.m_task;    }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_task = HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_PROCESS, jsonObj.opt(TASK));
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TASK, HAPJsonUtility.buildJson(this.m_task, HAPSerializationFormat.JSON));
	}

	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPEmbededTaskActivityDefinition out = new HAPEmbededTaskActivityDefinition(this.getType());
		this.cloneToTaskActivityDefinition(out);
		out.m_task = this.m_task.clone();
		return out;
	}
}
