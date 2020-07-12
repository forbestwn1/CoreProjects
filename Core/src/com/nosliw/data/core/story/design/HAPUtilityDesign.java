package com.nosliw.data.core.story.design;

import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPUtilityDesign {

	public static HAPDesignStory readStoryDesign(String id) {
		//read content
		String file = HAPSystemFolderUtility.getStoryDesignFolder()+id+".design";
		//parse content
		HAPDesignStory out = HAPParserStoryDesign.parseFile(file);
		return out;
	}
	
	public static void saveStoryDesign(HAPDesignStory storyDesign) {
		
	}
	
}
