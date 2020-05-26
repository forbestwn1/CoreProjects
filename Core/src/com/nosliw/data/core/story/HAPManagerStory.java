package com.nosliw.data.core.story;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.story.design.HAPBuilderStory;
import com.nosliw.data.core.story.design.HAPDesignStory;
import com.nosliw.data.core.story.resource.HAPResourceDefinitionStory;
import com.nosliw.data.core.story.resource.HAPResourceIdStory;
import com.nosliw.data.core.story.resource.HAPStoryId;

public class HAPManagerStory {

	private HAPManagerResourceDefinition m_resourceDefManager;

	private Map<String, HAPBuilderShow> m_resourceDefinitionBuilders;
	
	private Map<String, HAPBuilderStory> m_storyBuilders;
	
	public HAPManagerStory(HAPManagerResourceDefinition resourceDefManager) {
		this.m_resourceDefManager = resourceDefManager;
		this.m_resourceDefinitionBuilders = new LinkedHashMap<String, HAPBuilderShow>();
		this.m_storyBuilders = new LinkedHashMap<String, HAPBuilderStory>();
	}
	
	public HAPResourceDefinitionStory getStory(String id) {
		return (HAPResourceDefinitionStory)this.m_resourceDefManager.getResourceDefinition(new HAPResourceIdStory(new HAPStoryId(id)));
	}
	
	public HAPResourceDefinition buildShow(HAPStory story) {
		HAPResourceDefinition out = this.m_resourceDefinitionBuilders.get(story.getShowType()).buildShow(story);
		HAPUtilityStory.exportBuildResourceDefinition(story, out);
		return out;
	}

	public HAPDesignStory buildStory(HAPDesignStory design) {     return this.m_storyBuilders.get(design.getBuilder()).buildStory(design);	}
	
	public void registerShowBuilder(String id, HAPBuilderShow builder) {	this.m_resourceDefinitionBuilders.put(id, builder);	}
	
	public void registerStoryBuilder(String id, HAPBuilderStory storyBuilder) {		this.m_storyBuilders.put(id, storyBuilder);	}

}
