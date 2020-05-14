package com.nosliw.data.core.story;

import com.nosliw.data.core.component.HAPManagerResourceDefinition;
import com.nosliw.data.core.story.resource.HAPResourceDefinitionStory;
import com.nosliw.data.core.story.resource.HAPResourceIdStory;
import com.nosliw.data.core.story.resource.HAPStoryId;

public class HAPManagerStory {

	private HAPManagerResourceDefinition m_resourceDefManager;

	public HAPManagerStory(HAPManagerResourceDefinition resourceDefManager) {
		this.m_resourceDefManager = resourceDefManager;
	}
	
	public HAPResourceDefinitionStory getStory(String id) {
		return (HAPResourceDefinitionStory)this.m_resourceDefManager.getResourceDefinition(new HAPResourceIdStory(new HAPStoryId(id)));
	}
	
	
}
