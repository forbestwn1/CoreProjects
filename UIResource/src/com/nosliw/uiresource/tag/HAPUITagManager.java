package com.nosliw.uiresource.tag;

public class HAPUITagManager {

	
	
	
	public HAPUITagDefinition getUITagDefinition(HAPUITagId id){
		String rootPath = "C:\\Users\\ewaniwa\\Desktop\\MyWork\\ApplicationData\\tags";
		String fileName = rootPath + "\\" + id.getId() + ".js";
		
		HAPUITagDefinition out = HAPUITagDefinitionParser.parseFromFile(fileName);
		return out;
	}
	
	
	public static void main(String[] args){
		HAPUITagManager uiTagManager = new HAPUITagManager();
		HAPUITagDefinition def = uiTagManager.getUITagDefinition(new HAPUITagId("test"));
		System.out.println(def.toString());
	}
	
}
