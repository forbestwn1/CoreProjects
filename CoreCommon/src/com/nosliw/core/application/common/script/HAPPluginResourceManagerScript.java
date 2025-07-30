package com.nosliw.core.application.common.script;

import java.io.File;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.system.HAPSystemFolderUtility;
import com.nosliw.data.core.resource.HAPPluginResourceManager;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPPluginResourceManagerScript implements HAPPluginResourceManager{

	private String m_resourceType;
	
	public HAPPluginResourceManagerScript(String resourceType) {
		this.m_resourceType = resourceType;
	}

	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		String scriptId = simpleResourceId.getId();
		String scriptFileName = HAPSystemFolderUtility.getManualBrickBaseFolder() + this.m_resourceType + "/" + scriptId + ".js";
		File scriptFile = new File(scriptFileName);
		String script = HAPUtilityFile.readFile(scriptFile);
		
		HAPResourceDataScript scriptBrick = new HAPResourceDataScript();
		scriptBrick.setScript(script);
		
		return scriptBrick;
	}

}
