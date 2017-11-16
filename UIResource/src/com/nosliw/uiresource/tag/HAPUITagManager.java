package com.nosliw.uiresource.tag;

public class HAPUITagManager {

	
	
	
	public HAPUITagDefinition getUITagDefinition(String name){
		String rootPath = "C:\\Users\\ewaniwa\\Desktop\\MyWork\\ApplicationData\\tags";
		String fileName = rootPath + "\\" + name + ".js";
		
		HAPUITagDefinition out = HAPUITagDefinitionParser.parseFromFile(fileName);
		return out;
	}
	
	
	public static void main(String[] args){
		HAPUITagManager uiTagManager = new HAPUITagManager();
		HAPUITagDefinition def = uiTagManager.getUITagDefinition("test");
		System.out.println(def.toString());
	}
	
}
