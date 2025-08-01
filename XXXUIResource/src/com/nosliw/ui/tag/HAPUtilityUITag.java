package com.nosliw.ui.tag;

import java.io.File;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.system.HAPSystemFolderUtility;
import com.nosliw.core.xxx.resource.HAPInfoResourceLocation;
import com.nosliw.data.core.component.HAPPathLocationBase;

public class HAPUtilityUITag {

	public static String getUITagDefinitionFolder(String tagId) {
		return HAPSystemFolderUtility.getTagDefinitionFolder() + tagId + "/";
	}

	public static File getUITagDefinitionFile(String tagId) {
		String fileName = getUITagDefinitionFolder(tagId) + "definition.json";
		return new File(fileName);
	}
	
	public static File getUITagScriptFile(String tagId) {
		String fileName = getUITagDefinitionFolder(tagId) + "script.js";
		return new File(fileName);
	}
	
	public static HAPInfoResourceLocation getUITagDefinitionLocationInfo(String uiTagId) {
		return new HAPInfoResourceLocation(getUITagDefinitionFile(uiTagId), HAPSerializationFormat.JSON, new HAPPathLocationBase(getUITagDefinitionFolder(uiTagId)));
	}

	public static HAPInfoResourceLocation getUITagScriptLocationInfo(String uiTagId) {
		return new HAPInfoResourceLocation(getUITagScriptFile(uiTagId), HAPSerializationFormat.JAVASCRIPT, new HAPPathLocationBase(getUITagDefinitionFolder(uiTagId)));
	}
}
