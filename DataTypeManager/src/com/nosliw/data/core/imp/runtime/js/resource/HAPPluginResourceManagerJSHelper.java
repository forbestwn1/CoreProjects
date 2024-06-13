package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPPluginResourceManagerJSHelper extends HAPPluginResourceManagerWithDataAccess{

	@HAPAttribute
	public static final String INFO_OPERATIONINFO = "operationInfo";
	
	public HAPPluginResourceManagerJSHelper(HAPDataAccessRuntimeJS dataAccess){
		super(dataAccess);
	}
	
	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceDataHelperImp helperResource = this.getDataAccess().getResourceHelper(simpleResourceId.getCoreIdLiterate());
		return helperResource;
	}

}
