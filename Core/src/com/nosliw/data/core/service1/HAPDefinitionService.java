package com.nosliw.data.core.service1;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPDefinitionService extends HAPSerializableImp{

	@HAPAttribute
	public static String INFO = "info";

	@HAPAttribute
	public static String IMPLEMENTATION = "implementation";
	
	@HAPAttribute
	public static String CONFIGURE = "configure";
	
	private HAPDefinitionServiceInfo m_serviceInfo;
	
	//the implementation information for this service(name, class name)
	private String m_implementation;
	
	//configure 
	private Object m_configure;

	public HAPDefinitionServiceInfo getServiceInfo() {   return this.m_serviceInfo;   }
	
	public String getImplementation(){  return this.m_implementation;   }
	
	public Object getConfigure(){  return this.m_configure;   }

	public HAPDefinitionService(){
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			this.m_serviceInfo = new HAPDefinitionServiceInfo();
			this.m_serviceInfo.buildObjectByJson(objJson);
			
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
