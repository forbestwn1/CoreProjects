package com.nosliw.data.core.service.definition;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinitionOrId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

//contains all information related with service definition
@HAPEntityWithAttribute
public class HAPDefinitionService extends HAPResourceDefinition{

	public static final String CHILD_INTERFACE = "interface";
	
	@HAPAttribute
	public static String STATIC = "static";

	@HAPAttribute
	public static String RUNTIME = "runtime";

	//information used for configuration, management purpose
	private HAPInfoServiceStatic m_staticInfo;
	
	private boolean m_isProcessed = false;
	
	//information for how two create runtime executor 
	private HAPInfoServiceRuntime m_runtimeInfo;

	public HAPDefinitionService(){
	}
	
	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICE;  }
	
	@Override
	public HAPResourceDefinitionOrId getChild(String path) {
		HAPResourceDefinitionOrId out = null;
		if(CHILD_INTERFACE.equals(path)) {
			out = this.m_staticInfo.getInterface();
		}
		return out;
	}

	public HAPInfoServiceStatic getStaticInfo() {   return this.m_staticInfo;   }
	
	public HAPInfoServiceRuntime getRuntimeInfo() {  return this.m_runtimeInfo;  }
	
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
		jsonMap.put(STATIC, HAPJsonUtility.buildJson(this.m_staticInfo, HAPSerializationFormat.JSON));
		jsonMap.put(RUNTIME, HAPJsonUtility.buildJson(this.m_runtimeInfo, HAPSerializationFormat.JSON));
	}

}
