package com.nosliw.data.core.story.resource;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.system.HAPSystemFolderUtility;

public class HAPResourceDefinitionPluginStory implements HAPPluginResourceDefinition{

	public HAPResourceDefinitionPluginStory() {
	}
	
	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		//read content
		String file = HAPSystemFolderUtility.getStoryFolder()+resourceId.getId()+".story";
		//parse content
		HAPResourceDefinitionStory out = HAPParserStoryResource.parseFile(file);
		return out;
	}

	@Override
	public String getResourceType() {		return HAPConstant.RUNTIME_RESOURCE_TYPE_STORY;	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		JSONObject jsonObj = (JSONObject)content;
		return HAPParserStoryResource.parseStoryDefinition(jsonObj);
	}

}
