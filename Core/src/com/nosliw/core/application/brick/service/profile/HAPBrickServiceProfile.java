package com.nosliw.core.application.brick.service.profile;

import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.application.brick.service.interfacee.HAPBrickServiceInterface;
import com.nosliw.core.application.common.entityinfo.HAPBrickWithEntityInfoSimple;
import com.nosliw.core.application.service.HAPInfoServiceRuntime;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

//contains all information related with service definition
@HAPEntityWithAttribute
public class HAPBrickServiceProfile extends HAPBrickWithEntityInfoSimple{

	public static final String CHILD_INTERFACE = "interface";
	
	@HAPAttribute
	public static String TAG = "tag";

	@HAPAttribute
	public static String INTERFACE = "interface";

	private HAPBrickServiceInterface m_interface;

	private List<String> m_tags;
	
	//information used for configuration, management purpose
	private HAPInfoServiceStatic m_staticInfo;
	
	private boolean m_isProcessed = false;
	
	public HAPBrickServiceProfile(){
	}
	
	public HAPBrickServiceInterface getServiceInterface() {   return this.m_interface;   }
	
	public List<String> getTags(){   return this.m_tags;    }
	

	public void process(HAPRuntimeEnvironment runtimeEnv) {
		this.m_staticInfo.process(runtimeEnv);
		this.m_isProcessed = true;
	}
	
	public boolean isProcessed() {   return this.m_isProcessed;   }
	

	@Override
	protected boolean buildObjectByJson(Object json){
		try{
			JSONObject objJson = (JSONObject)json;
			
			this.m_staticInfo = new HAPInfoServiceStatic();
			this.m_staticInfo.buildObjectByJson(objJson.getJSONObject(STATIC));
			
			this.m_runtimeInfo = new HAPInfoServiceRuntime();
			this.m_runtimeInfo.buildObjectByJson(objJson.getJSONObject(RUNTIME));
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
		return true;  
	}
	
	@Override
	public void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(STATIC, HAPUtilityJson.buildJson(this.m_staticInfo, HAPSerializationFormat.JSON));
		jsonMap.put(RUNTIME, HAPUtilityJson.buildJson(this.m_runtimeInfo, HAPSerializationFormat.JSON));
	}

}
