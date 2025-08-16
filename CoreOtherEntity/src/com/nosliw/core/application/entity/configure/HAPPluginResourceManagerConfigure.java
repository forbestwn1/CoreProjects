package com.nosliw.core.application.entity.configure;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.resource.HAPPluginResourceManager;
import com.nosliw.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.system.HAPSystemFolderUtility;

public class HAPPluginResourceManagerConfigure implements HAPPluginResourceManager{

	public HAPPluginResourceManagerConfigure() {}

	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		String configureId = simpleResourceId.getId();
		String configureFileName = HAPSystemFolderUtility.getManualBrickBaseFolder() + HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONFIGURE + "/" + configureId + ".json";
		File configureFile = new File(configureFileName);
		String configureStr = HAPUtilityFile.readFile(configureFile);
		
		HAPResourceDataConfigure configureResourceData = new HAPResourceDataConfigure(new JSONObject(configureStr));
		return configureResourceData;
	}

}
