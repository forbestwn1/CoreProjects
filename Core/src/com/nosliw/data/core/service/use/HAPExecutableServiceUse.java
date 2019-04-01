package com.nosliw.data.core.service.use;

import java.util.List;
import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.resource.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPExecutableImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;

@HAPEntityWithAttribute
public class HAPExecutableServiceUse extends HAPExecutableImp{

	@HAPAttribute
	public static String PROVIDER = "provider";

	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String INFO = "info";

	@HAPAttribute
	public static String SERVICEMAPPING = "serviceMapping";

	private HAPExecutableWrapperTask m_serviceMapping;
	
	private HAPDefinitionServiceUse m_definition;
	
	public HAPExecutableServiceUse(HAPDefinitionServiceUse definition) {
		this.m_definition = definition;
	}
	
	public HAPExecutableWrapperTask getServiceMapping() {  return this.m_serviceMapping;   }
	public void setServiceMapping(HAPExecutableWrapperTask serviceMapping) {    this.m_serviceMapping = serviceMapping;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(PROVIDER, this.m_definition.getProvider());
		jsonMap.put(NAME, this.m_definition.getName());
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_definition.getInfo(), HAPSerializationFormat.JSON));
		jsonMap.put(SERVICEMAPPING, HAPJsonUtility.buildJson(this.m_serviceMapping, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildResourceJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap, HAPRuntimeInfo runtimeInfo) {
		super.buildResourceJsonMap(jsonMap, typeJsonMap, runtimeInfo);
		jsonMap.put(SERVICEMAPPING, this.m_serviceMapping.toResourceData(runtimeInfo).toString());
	}

	@Override
	protected void buildResourceDependency(List<HAPResourceDependent> dependency, HAPRuntimeInfo runtimeInfo) {
		dependency.addAll(this.m_serviceMapping.getResourceDependency(runtimeInfo));
	}
}
