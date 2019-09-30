package com.nosliw.data.core.service.provide;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;

//contains all information related with service definition
@HAPEntityWithAttribute
public class HAPDefinitionService extends HAPEntityInfoImp{

	@HAPAttribute
	public static String STATIC = "static";

	@HAPAttribute
	public static String RUNTIME = "runtime";

	//information used for configuration, management purpose
	private HAPInfoServiceStatic m_staticInfo;
	
	//information for how two create runtime executor 
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
			this.m_staticInfo.buildObjectByJson(objJson);
			
			this.m_runtimeInfo = new HAPInfoServiceRuntime();
			this.m_runtimeInfo.buildObjectByJson(objJson);

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
		jsonMap.put(STATIC, HAPJsonUtility.buildJson(this.m_staticInfo, HAPSerializationFormat.JSON));
		jsonMap.put(RUNTIME, HAPJsonUtility.buildJson(this.m_runtimeInfo, HAPSerializationFormat.JSON));
	}
	
}
