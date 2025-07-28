package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResource;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.imp.runtime.js.HAPResourceManagerDataAccess;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerJSConverter extends HAPResourceManagerDataAccess{

	public HAPResourceManagerJSConverter(HAPDataAccessRuntimeJS dataAccess, HAPManagerResource rootResourceMan){
		super(dataAccess, rootResourceMan);
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdConverter resourceIdOperation = new HAPResourceIdConverter((HAPResourceIdSimple)resourceId);
		HAPResourceDataJSConverterImp converterResource = this.getDataAccess().getDataTypeConverter(resourceIdOperation.getConverter());
		if(converterResource!=null)		return new HAPResource(resourceId, converterResource, null);
		else return null;
	}

}
