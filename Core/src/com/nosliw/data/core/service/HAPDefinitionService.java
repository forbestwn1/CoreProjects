package com.nosliw.data.core.service;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPDefinitionService extends HAPSerializableImp{

	@HAPAttribute
	public static String INFO = "info";

	@HAPAttribute
	public static String RUNTIME = "runtime";

	private HAPDefinitionServiceInfo m_serviceInfo;
	
	private HAPDefinitionServiceRuntime m_runtimeInfo;

	public HAPDefinitionService(){
	}
	
	public HAPDefinitionServiceInfo getServiceInfo() {   return this.m_serviceInfo;   }
	
	public HAPDefinitionServiceRuntime getRuntimeInfo() {  return this.m_runtimeInfo;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			this.m_serviceInfo = new HAPDefinitionServiceInfo();
			this.m_serviceInfo.buildObjectByJson(objJson.optJSONObject(INFO));
			
			this.m_runtimeInfo = new HAPDefinitionServiceRuntime();
			this.m_runtimeInfo.buildObjectByJson(objJson.optJSONObject(RUNTIME));

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
}
