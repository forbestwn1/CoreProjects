package com.nosliw.data.core.story.resource;

import com.nosliw.core.resource.HAPManagerResourceDefinition;

public class HAPManagerResourceStory {

	private HAPManagerResourceDefinition m_resourceDefManager;
	
	public HAPManagerResourceStory(HAPManagerResourceDefinition resourceDefManager) {
		this.m_resourceDefManager = resourceDefManager;
	}

	public HAPResourceDefinitionStory getStory(String id) {
		return (HAPResourceDefinitionStory)this.m_resourceDefManager.getLocalResourceDefinition(new HAPResourceIdStory(new HAPStoryId(id)));
	}
	
}
