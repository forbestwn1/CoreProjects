package com.nosliw.data.core.activity;

import java.io.File;

import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPManagerActivityPlugin {

	public HAPPluginActivity getPlugin(String type) {
		String fileName = HAPSystemFolderUtility.getActivityPluginFolder() + type + ".js";
		File file = new File(fileName);
		
		HAPPluginActivity out = HAPParserActivityPlugin.parseFromFile(file);
		return out;
	}
	
}
