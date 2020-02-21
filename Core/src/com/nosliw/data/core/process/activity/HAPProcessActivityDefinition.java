package com.nosliw.data.core.process.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.process.HAPDefinitionActivityTask;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdFactory;

public class HAPProcessActivityDefinition extends HAPDefinitionActivityTask{

	@HAPAttribute
	public static String PROCESS = "process";
	
	private HAPResourceId m_process;
	
	public HAPProcessActivityDefinition(String type) {
		super(type);
	}
	
	public HAPResourceId getProcess(){  return this.m_process;    }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_process = HAPResourceIdFactory.newInstance(HAPConstant.RUNTIME_RESOURCE_TYPE_PROCESS, jsonObj.opt(PROCESS));
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROCESS, HAPJsonUtility.buildJson(this.m_process, HAPSerializationFormat.JSON));
	}
}
