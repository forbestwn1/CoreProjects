package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerDataAccess;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;

public class HAPResourceManagerJSHelper extends HAPResourceManagerDataAccess{

	public HAPResourceManagerJSHelper(HAPDataAccessRuntimeJS dataAccess){
		super(dataAccess);
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId) {
		HAPResourceDataHelperImp helperResource = this.getDataAccess().getResourceHelper(resourceId.getId());
		if(helperResource!=null)		return new HAPResource(resourceId, helperResource, null);
		else return null;
	}

}
