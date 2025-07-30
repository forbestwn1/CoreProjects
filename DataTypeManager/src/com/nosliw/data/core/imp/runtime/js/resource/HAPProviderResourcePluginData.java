package com.nosliw.data.core.imp.runtime.js.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPFactoryResourceTypeId;
import com.nosliw.core.resource.HAPIdResourceType;
import com.nosliw.core.resource.HAPPluginResourceManager;
import com.nosliw.core.resource.HAPProviderResourcePlugin;
import com.nosliw.data.core.imp.HAPDataAccessDataType;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;

@Component
public class HAPProviderResourcePluginData implements HAPProviderResourcePlugin{

	Map<HAPIdResourceType, HAPPluginResourceManager> m_plugins = new LinkedHashMap<HAPIdResourceType, HAPPluginResourceManager>();
	
	HAPDataAccessRuntimeJS m_runtimeJSDataAccess;
	HAPDataAccessDataType m_dataTypeDataAccess;
	
	public HAPProviderResourcePluginData(HAPModuleRuntimeJS jsRuntimeModule) {
		this.m_runtimeJSDataAccess = jsRuntimeModule.getRuntimeJSDataAccess();
		this.m_dataTypeDataAccess = jsRuntimeModule.getDataTypeDataAccess();

		
		m_plugins.put(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_OPERATION), new HAPPluginResourceManagerJSOperation(m_runtimeJSDataAccess, m_dataTypeDataAccess));
		m_plugins.put(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONVERTER), new HAPPluginResourceManagerJSConverter(m_runtimeJSDataAccess));
		
		m_plugins.put(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSHELPER), new HAPPluginResourceManagerJSHelper(m_runtimeJSDataAccess));
//		this.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSGATEWAY, new HAPResourceManagerJSGateway(this));
	}
	
	@Override
	public Map<HAPIdResourceType, HAPPluginResourceManager> getResourceManagerPlugins() {
		return m_plugins;
	}

}
