package com.nosliw.data.core.activity.plugin;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;
import com.nosliw.data.core.activity.HAPExecutableActivity;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;

public class HAPServiceActivityExecutable extends HAPExecutableActivity{

	@HAPAttribute
	public static String SERVICE = "service";

	@HAPAttribute
	public static String PROVIDER = "provider";

	private HAPExecutableServiceUse m_service;
	
	private String m_provider;
	
	public HAPServiceActivityExecutable(String id, HAPDefinitionActivityNormal activityDef) {
		super(activityDef.getActivityType(), id, activityDef);
	}

	public void setService(HAPExecutableServiceUse service) {   this.m_service = service;  }
	public HAPExecutableServiceUse getService() {  return this.m_service;   }
	
	public void setServiceProvider(String provider) {   this.m_provider = provider;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SERVICE, HAPUtilityJson.buildJson(this.m_service, HAPSerializationFormat.JSON));
		jsonMap.put(PROVIDER, HAPUtilityJson.buildJson(this.m_provider, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(SERVICE, this.m_service.toResourceData(runtimeInfo).toString());
	}	
}
