package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;
import com.nosliw.data.core.process.HAPExecutableActivityNormal;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.service.use.HAPDefinitionServiceProvider;
import com.nosliw.data.core.service.use.HAPExecutableServiceUse;

public class HAPServiceActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String SERVICE = "service";

	@HAPAttribute
	public static String PROVIDER = "provider";

	private HAPExecutableServiceUse m_service;
	
	private HAPDefinitionServiceProvider m_provider;
	
	public HAPServiceActivityExecutable(String id, HAPDefinitionActivityNormal activityDef) {
		super(id, activityDef);
	}

	public void setService(HAPExecutableServiceUse service) {   this.m_service = service;  }
	public HAPExecutableServiceUse getService() {  return this.m_service;   }
	
	public void setServiceProvider(HAPDefinitionServiceProvider provider) {   this.m_provider = provider;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(SERVICE, HAPJsonUtility.buildJson(this.m_service, HAPSerializationFormat.JSON));
		jsonMap.put(PROVIDER, HAPJsonUtility.buildJson(this.m_provider, HAPSerializationFormat.JSON));
	}
	
	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(SERVICE, this.m_service.toResourceData(runtimeInfo).toString());
	}	
}
