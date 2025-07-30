package com.nosliw.data.core.story.resource;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPPluginResourceDefinition;
import com.nosliw.core.resource.HAPResourceDefinition1;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.system.HAPSystemFolderUtility;

public class HAPResourceDefinitionPluginStory implements HAPPluginResourceDefinition{

	public HAPResourceDefinitionPluginStory() {
	}
	
	@Override
	public HAPResourceDefinition1 getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId) {
		//read content
		String file = HAPSystemFolderUtility.getStoryFolder()+resourceId.getId()+".story";
		//parse content
		HAPResourceDefinitionStory out = HAPParserStoryResource.parseFile(file);
		return out;
	}

	@Override
	public String getResourceType() {		return HAPConstantShared.RUNTIME_RESOURCE_TYPE_STORY;	}

	@Override
	public HAPResourceDefinition1 parseResourceEntity(Object content) {
		JSONObject jsonObj = (JSONObject)content;
		return HAPParserStoryResource.parseStoryDefinition(jsonObj);
	}

}
