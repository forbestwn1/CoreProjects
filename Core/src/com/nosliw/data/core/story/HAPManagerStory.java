package com.nosliw.data.core.story;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.story.resource.HAPResourceDefinitionStory;
import com.nosliw.data.core.story.resource.HAPResourceIdStory;
import com.nosliw.data.core.story.resource.HAPStoryId;

public class HAPManagerStory {

	private HAPManagerResourceDefinition m_resourceDefManager;

	private Map<String, HAPResourceBuilder> m_resourceDefinitionBuilders;
	
	public HAPManagerStory(HAPManagerResourceDefinition resourceDefManager) {
		this.m_resourceDefManager = resourceDefManager;
		this.m_resourceDefinitionBuilders = new LinkedHashMap<String, HAPResourceBuilder>();
	}
	
	public void registerResourceBuilder(String id, HAPResourceBuilder builder) {	this.m_resourceDefinitionBuilders.put(id, builder);	}
	
	public HAPResourceDefinitionStory getStory(String id) {
		return (HAPResourceDefinitionStory)this.m_resourceDefManager.getResourceDefinition(new HAPResourceIdStory(new HAPStoryId(id)));
	}
	
	public HAPResourceDefinition buildResourceDefinition(HAPStory story) {
		HAPResourceDefinition out = this.m_resourceDefinitionBuilders.get(story.getResourceType()).buildResourceDefinition(story);
		HAPUtilityStory.exportBuildResourceDefinition(story, out);
		return out;
	}
}
