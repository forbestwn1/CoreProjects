package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResource;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.resource.HAPUtilityResource;
import com.nosliw.core.system.HAPSystemFolderUtility;
import com.nosliw.core.xxx.resource.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public class HAPResourceManagerUIModuleDecoration extends HAPResourceManagerImp{

	public HAPResourceManagerUIModuleDecoration(HAPManagerResource rootResourceMan) {
		super(rootResourceMan);
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdUIModuleDecoration id = new HAPResourceIdUIModuleDecoration((HAPResourceIdSimple)resourceId);
		String file = HAPSystemFolderUtility.getUIModuleDecorationFolder()+id.getCoreIdLiterate()+".js";
		return new HAPResource(resourceId, HAPResourceDataFactory.createJSValueResourceData(HAPUtilityFile.readFile(file)), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}

}
