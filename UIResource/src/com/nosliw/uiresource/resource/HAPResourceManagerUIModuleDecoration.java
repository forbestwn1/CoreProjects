package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPResourceManagerUIModuleDecoration extends HAPResourceManagerImp{

	public HAPResourceManagerUIModuleDecoration(HAPResourceManager rootResourceMan) {
		super(rootResourceMan);
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdUIModuleDecoration id = new HAPResourceIdUIModuleDecoration((HAPResourceIdSimple)resourceId);
		String file = HAPSystemFolderUtility.getUIModuleDecorationFolder()+id.getCoreIdLiterate()+".js";
		return new HAPResource(resourceId, HAPResourceDataFactory.createJSValueResourceData(HAPUtilityFile.readFile(file)), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}

}
