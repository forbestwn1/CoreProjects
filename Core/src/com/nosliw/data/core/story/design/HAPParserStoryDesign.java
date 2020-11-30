package com.nosliw.data.core.story.design;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.story.change.HAPManagerChange;

public class HAPParserStoryDesign {

	public static HAPDesignStory parseFile(String fileName, HAPManagerChange changeMan){
		HAPDesignStory out = null;
		try{
			File input = new File(fileName);
			String source = HAPFileUtility.readFile(input);
			out = parseContent(source, changeMan);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	private static HAPDesignStory parseContent(String content, HAPManagerChange changeMan) {
		JSONObject jsonObj = new JSONObject(content);
		HAPDesignStory out = parseStoryDefinition(jsonObj, changeMan);
		return out;
	}

	public static HAPDesignStory parseStoryDefinition(JSONObject jsonObj, HAPManagerChange changeMan) {
		HAPDesignStory out = new HAPDesignStory(changeMan);
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
}
