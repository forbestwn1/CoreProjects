package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.imp.HAPDataAccessDataType;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.runtime.js.HAPResourceManagerJS;

public class HAPResourceManagerJSImp extends HAPResourceManagerJS{

	public HAPResourceManagerJSImp(HAPDataAccessRuntimeJS runtimeJSDataAccess, HAPDataAccessDataType dataTypeDataAccess){
		this.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_OPERATION, new HAPResourceManagerJSOperation(runtimeJSDataAccess, dataTypeDataAccess));
		this.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_CONVERTER, new HAPResourceManagerJSConverter(runtimeJSDataAccess));
		this.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSLIBRARY, new HAPResourceManagerJSLibrary());
		this.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSHELPER, new HAPResourceManagerJSHelper(runtimeJSDataAccess));
		this.registerResourceManager(HAPConstant.RUNTIME_RESOURCE_TYPE_JSGATEWAY, new HAPResourceManagerJSGateway());
	}
	
}
