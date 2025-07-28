package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPPluginResourceManagerJSConverter extends HAPPluginResourceManagerWithDataAccess{

	@HAPAttribute
	public static final String INFO_OPERATIONINFO = "operationInfo";
	
	public HAPPluginResourceManagerJSConverter(HAPDataAccessRuntimeJS dataAccess){
		super(dataAccess);
	}
	
	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdConverter resourceIdOperation = new HAPResourceIdConverter(simpleResourceId);
		HAPResourceDataJSConverterImp converterResource = this.getDataAccess().getDataTypeConverter(resourceIdOperation.getConverter());
		return converterResource;
	}

}
