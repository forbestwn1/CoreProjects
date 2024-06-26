package com.nosliw.core.application.common.script;

import java.io.File;
import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.HAPPluginDivision;
import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPPluginDivisionScript implements HAPPluginDivision{

	private HAPManagerApplicationBrick m_brickManager;
	
	public HAPPluginDivisionScript(HAPManagerApplicationBrick brickManager) {
		this.m_brickManager = brickManager;
	}
	
	@Override
	public HAPBundle getBundle(HAPIdBrick brickId) {
		HAPIdBrickType brickTypeId = brickId.getBrickTypeId();
		
		String scriptFileName = HAPSystemFolderUtility.getManualEntityBaseFolder() + brickTypeId.getBrickType() + "/" + brickId.getId() + ".js";
		File scriptFile = new File(scriptFileName);
		String script = HAPUtilityFile.readFile(scriptFile);

		HAPBrick scriptBrick = null;
		if(this.m_brickManager.getBrickTypeInfo(brickTypeId).getIsComplex()) {
			scriptBrick = this.m_brickManager.newBrickInstance(brickTypeId); 
			((HAPBlockScriptComplex)scriptBrick).setScript(script);
		}
		else {
			scriptBrick = this.m_brickManager.newBrickInstance(brickTypeId);
			((HAPBlockScriptSimple)scriptBrick).setScript(script);
		}
		
		HAPBundle bundle = new HAPBundle();
		bundle.setBrickWrapper(new HAPWrapperBrickRoot(scriptBrick));
		return bundle;
	}

	@Override
	public Set<HAPIdBrickType> getBrickTypes() {
		Set<HAPIdBrickType> out = new HashSet<HAPIdBrickType>();
		out.add(HAPEnumBrickType.DECORATIONSCRIPT_100);
		return out;
	}

}
