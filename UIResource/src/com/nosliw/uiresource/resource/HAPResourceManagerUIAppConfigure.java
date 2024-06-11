package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPResourceManagerUIAppConfigure extends HAPResourceManagerImp{

	public HAPResourceManagerUIAppConfigure(HAPManagerResource rootResourceMan) {
		super(rootResourceMan);
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdUIAppConfigure id = new HAPResourceIdUIAppConfigure((HAPResourceIdSimple)resourceId);
		String file = HAPSystemFolderUtility.getUIAppConfigureFolder()+id.getCoreIdLiterate()+".js";
		return new HAPResource(resourceId, HAPResourceDataFactory.createJSValueResourceData(HAPUtilityFile.readFile(file)), null);
	}

}
