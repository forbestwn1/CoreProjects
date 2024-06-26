package com.nosliw.core.application.configure;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.resource.HAPPluginResourceManager;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPPluginResourceManagerConfigure implements HAPPluginResourceManager{

	public HAPPluginResourceManagerConfigure() {}

	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		String configureId = simpleResourceId.getId();
		String configureFileName = HAPSystemFolderUtility.getManualEntityBaseFolder() + HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONFIGURE + "/" + configureId + ".json";
		File configureFile = new File(configureFileName);
		String configureStr = HAPUtilityFile.readFile(configureFile);
		
		HAPResourceDataConfigure configureResourceData = new HAPResourceDataConfigure(new JSONObject(configureStr));
		return configureResourceData;
	}

}
