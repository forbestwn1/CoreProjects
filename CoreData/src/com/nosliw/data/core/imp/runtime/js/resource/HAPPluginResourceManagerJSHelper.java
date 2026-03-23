package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.data.HAPJSHelperId;
import com.nosliw.core.data.HAPResourceIdJSHelper;
import com.nosliw.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.core.resource.HAPResourceHelper;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;

public class HAPPluginResourceManagerJSHelper extends HAPPluginResourceManagerWithDataAccess{

	@HAPAttribute
	public static final String INFO_OPERATIONINFO = "operationInfo";
	
	public HAPPluginResourceManagerJSHelper(HAPDataAccessRuntimeJS dataAccess){
		super(dataAccess);
		HAPResourceHelper.getInstance().registerResourceId(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSHELPER, HAPResourceIdJSHelper.class, HAPJSHelperId.class);
	}
	
	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceDataHelperImp helperResource = this.getDataAccess().getResourceHelper(simpleResourceId.getCoreIdLiterate());
		return helperResource;
	}

}
