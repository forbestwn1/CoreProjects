package com.nosliw.data.core.story.design;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPFileUtility;

public class HAPParserStoryDesign {

	public static HAPDesignStory parseFile(String fileName){
		HAPDesignStory out = null;
		try{
			File input = new File(fileName);
			String source = HAPFileUtility.readFile(input);
			out = parseContent(source);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	private static HAPDesignStory parseContent(String content) {
		JSONObject jsonObj = new JSONObject(content);
		HAPDesignStory out = parseStoryDefinition(jsonObj);
		return out;
	}

	public static HAPDesignStory parseStoryDefinition(JSONObject jsonObj) {
		HAPDesignStory out = new HAPDesignStory();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}
	
}
