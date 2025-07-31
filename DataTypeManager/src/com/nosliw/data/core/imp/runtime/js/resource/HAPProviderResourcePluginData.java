package com.nosliw.data.core.imp.runtime.js.resource;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPFactoryResourceTypeId;
import com.nosliw.core.resource.HAPProviderResourcePluginImp;
import com.nosliw.data.core.imp.HAPDataAccessDataType;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPModuleRuntimeJS;

@Component
public class HAPProviderResourcePluginData extends HAPProviderResourcePluginImp{

	HAPDataAccessRuntimeJS m_runtimeJSDataAccess;
	HAPDataAccessDataType m_dataTypeDataAccess;
	
	public HAPProviderResourcePluginData(HAPModuleRuntimeJS jsRuntimeModule) {
		this.m_runtimeJSDataAccess = jsRuntimeModule.getRuntimeJSDataAccess();
		this.m_dataTypeDataAccess = jsRuntimeModule.getDataTypeDataAccess();

		this.registerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_OPERATION), new HAPPluginResourceManagerJSOperation(m_runtimeJSDataAccess, m_dataTypeDataAccess));
		this.registerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONVERTER), new HAPPluginResourceManagerJSConverter(m_runtimeJSDataAccess));
		
		this.registerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSHELPER), new HAPPluginResourceManagerJSHelper(m_runtimeJSDataAccess));
//		this.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSGATEWAY, new HAPResourceManagerJSGateway(this));
	}
	
}
