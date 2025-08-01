package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResource;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.system.HAPSystemFolderUtility;
import com.nosliw.core.xxx.resource.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

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
