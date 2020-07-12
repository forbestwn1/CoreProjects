package com.nosliw.data.core.story;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.story.design.HAPBuilderStory;
import com.nosliw.data.core.story.design.HAPDesignStory;
import com.nosliw.data.core.story.design.HAPRequestChange;
import com.nosliw.data.core.story.design.HAPUtilityDesign;
import com.nosliw.data.core.story.resource.HAPResourceDefinitionStory;
import com.nosliw.data.core.story.resource.HAPResourceIdStory;
import com.nosliw.data.core.story.resource.HAPStoryId;

public class HAPManagerStory {

	private long m_idIndex;
	
	private HAPManagerResourceDefinition m_resourceDefManager;

	private Map<String, HAPBuilderShow> m_resourceDefinitionBuilders;
	
	private Map<String, HAPBuilderStory> m_storyBuilders;
	
	public HAPManagerStory(HAPManagerResourceDefinition resourceDefManager) {
		this.m_resourceDefManager = resourceDefManager;
		this.m_resourceDefinitionBuilders = new LinkedHashMap<String, HAPBuilderShow>();
		this.m_storyBuilders = new LinkedHashMap<String, HAPBuilderStory>();
		this.m_idIndex = System.currentTimeMillis();
	}
	
	public HAPDesignStory getStoryDesign(String id) {
		return HAPUtilityDesign.readStoryDesign(id);
	}
	
	public HAPDesignStory newStoryDesign(String builderId) {
		HAPBuilderStory storyBuilder = this.getStoryBuilder(builderId);
		HAPDesignStory out = storyBuilder.newDesign(this.generateId());
		return out;
	}
	
	public void saveStoryDesign(HAPDesignStory storyDesign) {
		
	}
	
	public HAPServiceData designStory(HAPRequestChange changeRequest) {
		
//		HAPDesignStory design = this.getStoryDesign(designChange.getDesignId());
//		return this.m_storyBuilders.get(design.getBuilder()).buildStory(design);
		return null;
	}

	//get story by id
	public HAPResourceDefinitionStory getStoryResource(String id) {
		return (HAPResourceDefinitionStory)this.m_resourceDefManager.getResourceDefinition(new HAPResourceIdStory(new HAPStoryId(id)));
	}

	//convert story to particular resouce
	public HAPResourceDefinition buildShow(HAPStory story) {
		HAPResourceDefinition out = this.m_resourceDefinitionBuilders.get(story.getShowType()).buildShow(story);
		HAPUtilityStory.exportBuildResourceDefinition(story, out);
		return out;
	}

	public void registerShowBuilder(String id, HAPBuilderShow builder) {	this.m_resourceDefinitionBuilders.put(id, builder);	}
	
	public void registerStoryBuilder(String id, HAPBuilderStory storyBuilder) {		this.m_storyBuilders.put(id, storyBuilder);	}

	private HAPBuilderStory getStoryBuilder(String buildId) {    return this.m_storyBuilders.get(buildId);   }
	private String generateId() {		return (this.m_idIndex++) + "";	}
}
