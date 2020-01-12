package com.nosliw.data.core.component;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPLifecycleAction extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String PROCESS = "process";
	
	private HAPDefinitionWrapperTask<String> m_process;

	public static HAPLifecycleAction newInstance(JSONObject jsonObj) {
		HAPLifecycleAction out = new HAPLifecycleAction();
		out.buildObjectByJson(jsonObj);
		return out;
	}
	
	public HAPDefinitionWrapperTask<String> getProcess(){   return this.m_process;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROCESS, this.m_process.toStringValue(HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		
		this.m_process = new HAPDefinitionWrapperTask<String>();
		this.m_process.setTaskDefinition(jsonObj.optString(PROCESS));
		this.m_process.buildMapping(jsonObj);
		return true;  
	}
}
