package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.imp.HAPDataAccessDataType;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.resource.HAPFactoryResourceTypeId;
import com.nosliw.data.core.runtime.js.HAPManagerResourceJS;

public class HAPManagerResourceJSImp extends HAPManagerResourceJS{

	public HAPManagerResourceJSImp(HAPDataAccessRuntimeJS runtimeJSDataAccess, HAPDataAccessDataType dataTypeDataAccess){
		this.registerResourceManagerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_OPERATION), new HAPPluginResourceManagerJSOperation(runtimeJSDataAccess, dataTypeDataAccess));
		this.registerResourceManagerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONVERTER), new HAPPluginResourceManagerJSConverter(runtimeJSDataAccess));
		
		
//		this.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSLIBRARY, new HAPResourceManagerJSLibrary(this));
		this.registerResourceManagerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSLIBRARY), new HAPPluginResourceManagerJSLibrary());
		
		
		this.registerResourceManagerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSHELPER), new HAPPluginResourceManagerJSHelper(runtimeJSDataAccess));
//		this.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSGATEWAY, new HAPResourceManagerJSGateway(this));
	}
	
}
