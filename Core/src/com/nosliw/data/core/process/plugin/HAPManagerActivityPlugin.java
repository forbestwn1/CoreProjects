package com.nosliw.data.core.process.plugin;

import java.io.File;

import com.nosliw.common.utils.HAPFileUtility;

public class HAPManagerActivityPlugin {

	public HAPPluginActivity getPlugin(String type) {
		String fileName = HAPFileUtility.getActivityPluginFolder() + type + ".js";
		File file = new File(fileName);
		
		HAPPluginActivity out = HAPParserActivityPlugin.parseFromFile(file);
		return out;
	}
	
}
