package com.nosliw.core.application.division.story.design.wizard.servicedriven;

import com.nosliw.core.application.division.story.HAPStoryReferenceElement;
import com.nosliw.core.application.division.story.HAPStoryStory;

public class HAPStoryUIChild {

	private HAPStoryStory m_story;

	private HAPStoryUINode m_node;
	
	private Object m_childId;

	private HAPStoryReferenceElement m_connectionRef;

	public HAPStoryUIChild(HAPStoryUINode node, Object childId, HAPStoryReferenceElement connectionRef, HAPStoryStory story) {
		this.m_node = node;
		this.m_childId = childId;
		this.m_connectionRef = connectionRef;
		this.m_story = story;
	}

	public HAPStoryUIChild(HAPStoryReferenceElement storyNodeRef, Object childId, HAPStoryReferenceElement connectionRef, HAPStoryStory story) {
		this(new HAPStoryUINode(storyNodeRef, story), childId, connectionRef, story);
	}

	public HAPStoryUINode getUINode() {    return this.m_node;     }
	
	public Object getChildId() {    return this.m_childId;     }
	
	public HAPStoryReferenceElement getConnectionRef() {    return this.m_connectionRef;     }
	
}
