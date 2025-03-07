package com.nosliw.data.core.story.resource;

import java.io.File;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.application.division.story.HAPStoryParserStory;
import com.nosliw.core.application.division.story.HAPStoryStoryImp;
import com.nosliw.data.core.component.HAPParserEntityComponent;

public class HAPParserStoryResource {

	public static HAPResourceDefinitionStory parseFile(String fileName){
		HAPResourceDefinitionStory out = null;
		try{
			File input = new File(fileName);
			//use file name as ui resource id
			String resourceId = HAPUtilityFile.getFileName(input);
			String source = HAPUtilityFile.readFile(input);
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
		HAPParserEntityComponent.parseComplextResourceDefinition(out, jsonObj);

		HAPStoryStoryImp storyEntity = HAPStoryParserStory.parseStory(jsonObj);
		out.setStory(storyEntity);
		
		return out;
	}
	
}
