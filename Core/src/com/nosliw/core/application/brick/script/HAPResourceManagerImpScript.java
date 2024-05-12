package com.nosliw.core.application.brick.script;

import java.io.File;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.data.core.resource.HAPPluginResourceManager;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPResourceManagerImpScript implements HAPPluginResourceManager{

	public HAPResourceManagerImpScript() {
	}

	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		String scriptId = simpleResourceId.getId();
		String scriptFileName = HAPSystemFolderUtility.getManualEntityBaseFolder() + HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT + "/" + scriptId + ".js";
		File scriptFile = new File(scriptFileName);
		String script = HAPUtilityFile.readFile(scriptFile);
		
		HAPBrickScript scriptBrick = new HAPBrickScript();
		scriptBrick.setScript(script);
		
		return new HAPResource(resourceId, scriptBrick.toResourceData(runtimeInfo), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}

	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdSimple simpleResourceId = (HAPResourceIdSimple)resourceId; 
		String scriptId = simpleResourceId.getId();
		String scriptFileName = HAPSystemFolderUtility.getManualEntityBaseFolder() + HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT + "/" + scriptId + ".js";
		File scriptFile = new File(scriptFileName);
		String script = HAPUtilityFile.readFile(scriptFile);
		
		HAPBrickScript scriptBrick = new HAPBrickScript();
		scriptBrick.setScript(script);
		
		return new HAPResource(resourceId, scriptBrick.toResourceData(runtimeInfo), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
//		return new HAPResource(resourceId, HAPResourceDataFactory.createJSValueResourceData(script), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}

}
