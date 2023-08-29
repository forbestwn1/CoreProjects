package com.nosliw.ui.tag;

import java.io.File;

import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.resource.HAPInfoResourceLocation;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityUITag {

	public static String getUITagDefinitionFolder(String tagId) {
		return HAPSystemFolderUtility.getTagDefinitionFolder() + tagId + "/";
	}

	public static File getUITagDefinitionFile(String tagId) {
		String fileName = getUITagDefinitionFolder(tagId) + "definition.json";
		return new File(fileName);
	}
	
	public static HAPInfoResourceLocation getUITagDefinitionLocationInfo(String uiTagId) {
		return new HAPInfoResourceLocation(getUITagDefinitionFile(uiTagId), new HAPPathLocationBase(getUITagDefinitionFolder(uiTagId)));
	}
}
