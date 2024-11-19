package com.nosliw.core.application.common.script;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPPluginDivision;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPPluginDivisionScript implements HAPPluginDivision{

	public HAPPluginDivisionScript() {
	}
	
	@Override
	public HAPBundle getBundle(HAPIdBrick brickId) {
		HAPIdBrickType brickTypeId = brickId.getBrickTypeId();
		
		String scriptFileName = HAPSystemFolderUtility.getManualBrickBaseFolder() + brickTypeId.getBrickType() + "/" + brickId.getId() + ".js";
		File scriptFile = new File(scriptFileName);
		String script = HAPUtilityFile.readFile(scriptFile);

		HAPBrickScript scriptBrick = new HAPBrickScript(brickTypeId);
		scriptBrick.setScript(script);
		
		HAPBundle bundle = new HAPBundle();
		bundle.setMainBrickWrapper(new HAPWrapperBrickRoot(scriptBrick));
		return bundle;
	}

	@Override
	public Set<HAPIdBrickType> getBrickTypes() {
		Set<HAPIdBrickType> out = new HashSet<HAPIdBrickType>();
		out.add(HAPEnumBrickType.DECORATIONSCRIPT_100);
		return out;
	}

}
