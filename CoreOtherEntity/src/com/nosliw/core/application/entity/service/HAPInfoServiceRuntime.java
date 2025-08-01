package com.nosliw.core.application.entity.service;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

//information related with service needed during runtime
public class HAPInfoServiceRuntime extends HAPSerializableImp{

	@HAPAttribute
	public static String IMPLEMENTATION = "implementation";
	
	@HAPAttribute
	public static String CONFIGURE = "configure";
	
	//the implementation information for this service(name, class name)
	private String m_implementation;
	
	//configure 
	private Object m_configure;

	public HAPInfoServiceRuntime() {}

	public String getImplementation(){  return this.m_implementation;   }
	
	public Object getConfigure(){  return this.m_configure;   }

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			this.m_implementation = (String)objJson.opt(IMPLEMENTATION);
			this.m_configure = objJson.opt(CONFIGURE);
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(IMPLEMENTATION, this.m_implementation);
		if(this.m_configure!=null)  jsonMap.put(CONFIGURE, this.m_configure.toString());
	}
	
}
