package com.nosliw.data.core.service.use;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

@HAPEntityWithAttribute
public class HAPExecutableServiceUse extends HAPExecutableImp{

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String INFO = "info";

	@HAPAttribute
	public static String SERVICEUSE = "serviceUse";

	@HAPAttribute
	public static String PROVIDERMAPPING = "providerMapping";

	@HAPAttribute
	public static String PROVIDERID = "providerId";

	//how to use service
	//variable to service interface
	private HAPExecutableWrapperTask m_serviceUse;
	
	//provider mapping to service interface
	//
	private HAPExecutableProviderToUse m_providerMapping;
	
	//provider service id
	private String m_providerId;
	
	private String m_name;
	
	private HAPInfo m_info;
	
	public HAPExecutableServiceUse(HAPDefinitionServiceUse definition) {
		this.m_name = definition.getName();
		this.m_info = definition.getInfo();
	}
	
	public HAPExecutableWrapperTask getServiceMapping() {  return this.m_serviceUse;   }
	public void setServiceMapping(HAPExecutableWrapperTask serviceMapping) {    this.m_serviceUse = serviceMapping;    }
	
	public HAPExecutableProviderToUse getProviderMapping() {  return this.m_providerMapping;   }
	public void setProviderMapping(HAPExecutableProviderToUse providerMapping) {    this.m_providerMapping = providerMapping;    }
	
	public void setProviderId(String providerId){    this.m_providerId = providerId;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(INFO, HAPUtilityJson.buildJson(this.m_info, HAPSerializationFormat.JSON));
		jsonMap.put(SERVICEUSE, HAPUtilityJson.buildJson(this.m_serviceUse, HAPSerializationFormat.JSON));
		jsonMap.put(PROVIDERMAPPING, HAPUtilityJson.buildJson(this.m_providerMapping, HAPSerializationFormat.JSON));
		jsonMap.put(PROVIDERID, this.m_providerId);
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(SERVICEUSE, this.m_serviceUse.toResourceData(runtimeInfo).toString());
		if(this.m_providerMapping!=null)   jsonMap.put(PROVIDERMAPPING, this.m_providerMapping.toResourceData(runtimeInfo).toString());
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependency> dependency, HAPRuntimeInfo runtimeInfo, HAPResourceManager resourceManager) {
		dependency.addAll(this.m_serviceUse.getResourceDependency(runtimeInfo, resourceManager));
		if(this.m_providerMapping!=null)   dependency.addAll(this.m_providerMapping.getResourceDependency(runtimeInfo, resourceManager));
	}
}
