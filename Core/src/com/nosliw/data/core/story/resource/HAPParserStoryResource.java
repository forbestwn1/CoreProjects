package com.nosliw.data.core.story.resource;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPParserComponent;
import com.nosliw.data.core.story.HAPParserStory;
import com.nosliw.data.core.story.HAPStoryImp;

public class HAPParserStoryResource {

	public static HAPResourceDefinitionStory parseFile(String fileName){
		HAPResourceDefinitionStory out = null;
		try{
			File input = new File(fileName);
			//use file name as ui resource id
			String resourceId = HAPFileUtility.getFileName(input);
			String source = HAPFileUtility.readFile(input);
			out = parseContent(source, resourceId);
		}
		catch(Exception e){
			e.printStackTrace();
		}
		return out;
	}

	private static HAPResourceDefinitionStory parseContent(String content, String id) {
		JSONObject jsonObj = new JSONObject(content);
		HAPResourceDefinitionStory out = parseStoryDefinition(jsonObj);
		out.setId(id);
		return out;
	}

	private static HAPResourceDefinitionStory parseStoryDefinition(JSONObject jsonObj) {
		HAPResourceDefinitionStory out = new HAPResourceDefinitionStory();

		//build complex resource part from json object
		HAPParserComponent.parseComplextResourceDefinition(out, jsonObj);

		HAPStoryImp storyEntity = HAPParserStory.parseStory(jsonObj);
		out.setStory(storyEntity);
		
		return out;
	}
	
}
