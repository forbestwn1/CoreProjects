package com.nosliw.data.core.service;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;

@HAPEntityWithAttribute
public class HAPDefinitionService extends HAPEntityInfoImp{

	@HAPAttribute
	public static String INFO = "info";

	@HAPAttribute
	public static String RUNTIME = "runtime";

	//static information
	private HAPInfoServiceStatic m_staticInfo;
	
	//runtime service instance information
	private HAPInfoServiceRuntime m_runtimeInfo;

	public HAPDefinitionService(){
	}
	
	public HAPInfoServiceStatic getStaticInfo() {   return this.m_staticInfo;   }
	
	public HAPInfoServiceRuntime getRuntimeInfo() {  return this.m_runtimeInfo;  }
	
	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			this.m_staticInfo = new HAPInfoServiceStatic();
			this.m_staticInfo.buildObjectByJson(objJson.optJSONObject(INFO));
			
			this.m_runtimeInfo = new HAPInfoServiceRuntime();
			this.m_runtimeInfo.buildObjectByJson(objJson.optJSONObject(RUNTIME));

		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
}
