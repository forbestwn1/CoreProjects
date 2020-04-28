package com.nosliw.uiresource.page.tag;

import java.io.File;

import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUITagManager {

	public HAPUITagQueryResult getDefaultUITagDefnition(HAPUITageQuery query) {
		HAPUITagQueryResult result = null;
		HAPDataTypeId dataTypeId = query.getDataTypeId();
		if(dataTypeId.getName().equals("test.string;1.0.0")) {
			result = new HAPUITagQueryResult("textinput");
		}
		return result;
	}
	
	public HAPUITagDefinition getUITagDefinition(HAPUITagId id){
		String fileName = HAPSystemFolderUtility.getTagDefinitionFolder() + id.getId() + ".js";
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
