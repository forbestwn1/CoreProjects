package com.nosliw.core.application.division.story.design.wizard;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.division.story.change.HAPStoryManagerChange;

public class HAPStoryParserStoryDesign {

	public static HAPStoryDesignStory parseFile(String fileName, HAPStoryManagerChange changeMan){
		HAPStoryDesignStory out = null;
		try{
			File input = new File(fileName);
			String source = HAPUtilityFile.readFile(input);
			out = parseContent(source, changeMan);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	private static HAPStoryDesignStory parseContent(String content, HAPStoryManagerChange changeMan) {
		JSONObject jsonObj = new JSONObject(content);
		HAPStoryDesignStory out = parseStoryDefinition(jsonObj, changeMan);
		return out;
	}

	public static HAPStoryDesignStory parseStoryDefinition(JSONObject jsonObj, HAPStoryManagerChange changeMan) {
		HAPStoryDesignStory out = new HAPStoryDesignStory(changeMan);
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
}
