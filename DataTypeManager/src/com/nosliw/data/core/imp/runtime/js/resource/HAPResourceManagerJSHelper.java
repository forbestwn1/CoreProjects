package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerDataAccess;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerJSHelper extends HAPResourceManagerDataAccess{

	public HAPResourceManagerJSHelper(HAPDataAccessRuntimeJS dataAccess){
		super(dataAccess);
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceDataHelperImp helperResource = this.getDataAccess().getResourceHelper(resourceId.getId());
		if(helperResource!=null)		return new HAPResource(resourceId, helperResource, null);
		else return null;
	}

}
