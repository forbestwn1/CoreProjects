package com.nosliw.data.core.service;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPDefinitionServiceRuntime extends HAPSerializableImp{

	@HAPAttribute
	public static String IMPLEMENTATION = "implementation";
	
	@HAPAttribute
	public static String CONFIGURE = "configure";
	
	//the implementation information for this service(name, class name)
	private String m_implementation;
	
	//configure 
	private Object m_configure;

	public HAPDefinitionServiceRuntime() {}

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
}
