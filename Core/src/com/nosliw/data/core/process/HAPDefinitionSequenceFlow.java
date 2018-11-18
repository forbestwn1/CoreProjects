package com.nosliw.data.core.process;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

//connection between two activity
public class HAPDefinitionSequenceFlow extends HAPSerializableImp{

	@HAPAttribute
	public static String TARGET = "target";
	
	private String m_target;
	
	public String getTarget() {   return this.m_target;  }
	
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			super.buildObjectByJson(json);
			JSONObject jsonObj = (JSONObject)json;
			this.m_target = (String)jsonObj.opt(TARGET);
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
		jsonMap.put(TARGET, m_target);
	}
	
}
