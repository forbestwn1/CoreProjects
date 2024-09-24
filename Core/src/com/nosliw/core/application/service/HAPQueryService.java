package com.nosliw.core.application.service;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

/*
 * Container information to locate service provider instance
 */
@HAPEntityWithAttribute
public class HAPQueryService extends HAPSerializableImp{

	@HAPAttribute
	public static String SERVICEID = "serviceId";

	@HAPAttribute
	public static String INTERFACE = "interface";

	private String m_serviceId;
	
	public String getServiceId() {   return this.m_serviceId;  }
	public void setServiceId(String id) {  this.m_serviceId = id;  }

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			this.m_serviceId = (String)objJson.opt(SERVICEID);
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
		jsonMap.put(SERVICEID, this.m_serviceId);
	}
}
