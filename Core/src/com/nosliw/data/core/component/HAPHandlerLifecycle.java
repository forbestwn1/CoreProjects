package com.nosliw.data.core.component;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.process.HAPEmbededProcessTask;

public class HAPHandlerLifecycle extends HAPEmbededProcessTask{

	@HAPAttribute
	public static String NAME = "name";

	private String m_name;

	public String getName() {   return this.m_name;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, this.m_name);
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = jsonObj.getString(NAME);
		return true;  
	}
	
	public HAPHandlerLifecycle cloneLifecycleHander() {
		HAPHandlerLifecycle out = new HAPHandlerLifecycle();
		this.cloneToEmbededProcessTask(out);
		out.m_name = this.m_name;
		return out;
	}
	
}
