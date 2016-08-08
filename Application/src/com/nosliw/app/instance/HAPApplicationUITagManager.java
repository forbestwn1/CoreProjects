package com.nosliw.app.instance;

import java.io.File;
import java.util.Set;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.uiresource.HAPUITagInfo;
import com.nosliw.uiresource.HAPUITagManager;

public class HAPApplicationUITagManager extends HAPUITagManager{

	HAPApplicationUITagManager(HAPConfigure configure) {
		super(configure);
		
		String tagLibPath = configure.getStringValue("libs.path");

		Set<File> tagFiles = HAPFileUtility.getAllFiles(tagLibPath);
		for(File tagFile : tagFiles){
			String name = HAPFileUtility.getFileName(tagFile);
			String tagScript = this.getTagScript(tagFile);
			HAPUITagInfo tagInfo = new HAPUITagInfo(name, tagScript);
			this.registerUITag(tagInfo);
		}
	}
}
