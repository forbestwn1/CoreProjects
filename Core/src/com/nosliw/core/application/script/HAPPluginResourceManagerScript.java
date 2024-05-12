package com.nosliw.core.application.script;

import java.io.File;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.resource.HAPPluginResourceManager;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPPluginResourceManagerScript implements HAPPluginResourceManager{

	public HAPPluginResourceManagerScript() {}

	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		String scriptId = simpleResourceId.getId();
		String scriptFileName = HAPSystemFolderUtility.getManualEntityBaseFolder() + HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT + "/" + scriptId + ".js";
		File scriptFile = new File(scriptFileName);
		String script = HAPUtilityFile.readFile(scriptFile);
		
		HAPResourceDataScript scriptBrick = new HAPResourceDataScript();
		scriptBrick.setScript(script);
		
		return scriptBrick;
	}

}
