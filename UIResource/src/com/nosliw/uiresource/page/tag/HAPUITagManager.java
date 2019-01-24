package com.nosliw.uiresource.page.tag;

import java.io.File;

import com.nosliw.common.utils.HAPFileUtility;

public class HAPUITagManager {

	public HAPUITagDefinition getUITagDefinition(HAPUITagId id){
		String fileName = HAPFileUtility.getTagDefinitionFolder() + id.getId() + ".js";
		File file = new File(fileName);
		
		HAPUITagDefinition out = HAPUITagDefinitionParser.parseFromFile(file);
		out.setSourceFile(file);
		
		return out;
	}
	
	
	public static void main(String[] args){
		HAPUITagManager uiTagManager = new HAPUITagManager();
		HAPUITagDefinition def = uiTagManager.getUITagDefinition(new HAPUITagId("test"));
		System.out.println(def.toString());
	}
	
}
