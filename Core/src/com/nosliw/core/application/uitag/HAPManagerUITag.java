package com.nosliw.core.application.uitag;

import java.io.File;
import java.util.Set;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPIdBrick;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPManagerUITag{

	public HAPManagerUITag() {}
	
	public HAPUITagDefinition getUITagDefinition(String tagId, String version) {
		if(version==null) {
			version = "1.0.0";
		}
		String fileName = getUITagFolder(tagId, version) + "definition.json";
		JSONObject jsonObj = new JSONObject(HAPUtilityFile.readFile(new File(fileName)));
		return HAPUtilityUITagDefinitionParser.parseUITagDefinition(jsonObj);
	}
	
	
	@Override
	public HAPBundle getBundle(HAPIdBrick brickId) {
		return null;
	}

	@Override
	public Set<HAPIdBrickType> getBrickTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	private String getUITagFolder(String tagId, String version) {
		return HAPSystemFolderUtility.getTagDefinitionFolder() + version + "/" + tagId +"/";
	}

	public static File getUITagScriptFile(String tagId) {
		String fileName = getUITagDefinitionFolder(tagId) + "script.js";
		return new File(fileName);
	}
	
}
