package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.imp.HAPDataAccessDataType;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.resource.HAPIdResourceType;
import com.nosliw.data.core.runtime.js.HAPManagerResourceJS;

public class HAPManagerResourceJSImp extends HAPManagerResourceJS{

	public HAPManagerResourceJSImp(HAPDataAccessRuntimeJS runtimeJSDataAccess, HAPDataAccessDataType dataTypeDataAccess){
		this.registerResourceManagerPlugin(new HAPIdResourceType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_OPERATION, "1.0.0"), new HAPPluginResourceManagerJSOperation(runtimeJSDataAccess, dataTypeDataAccess));
		this.registerResourceManagerPlugin(new HAPIdResourceType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONVERTER, "1.0.0"), new HAPPluginResourceManagerJSConverter(runtimeJSDataAccess));
		
		
//		this.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSLIBRARY, new HAPResourceManagerJSLibrary(this));
		this.registerResourceManagerPlugin(new HAPIdResourceType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSLIBRARY, "1.0.0"), new HAPPluginResourceManagerJSLibrary());
		
		
		this.registerResourceManagerPlugin(new HAPIdResourceType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSHELPER, "1.0.0"), new HAPPluginResourceManagerJSHelper(runtimeJSDataAccess));
//		this.registerResourceManager(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSGATEWAY, new HAPResourceManagerJSGateway(this));
	}
	
}
