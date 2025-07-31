package com.nosliw.core.application.entity.script;

import java.io.File;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.resource.HAPPluginResourceManager;
import com.nosliw.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.system.HAPSystemFolderUtility;

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
