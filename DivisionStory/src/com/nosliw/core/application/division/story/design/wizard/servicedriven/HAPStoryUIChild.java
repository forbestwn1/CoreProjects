package com.nosliw.core.application.division.story.design.wizard.servicedriven;

import com.nosliw.core.application.division.story.HAPStoryAliasElement;
import com.nosliw.core.application.division.story.HAPStoryIdElement;
import com.nosliw.core.application.division.story.HAPStoryReferenceElement;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUI;

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

	//for new node
	public HAPStoryUIChild(HAPStoryNodeUI storyNode, HAPStoryAliasElement alias, Object childId, HAPStoryReferenceElement connectionRef, HAPStoryStory story) {
		this(new HAPStoryUINode(storyNode, alias, story), childId, connectionRef, story);
	}

	//for solid node
	public HAPStoryUIChild(HAPStoryIdElement storyNodeId, Object childId, HAPStoryReferenceElement connectionRef, HAPStoryStory story) {
		this(new HAPStoryUINode(storyNodeId, story), childId, connectionRef, story);
	}

	public HAPStoryUINode getUINode() {    return this.m_node;     }
	
	public Object getChildId() {    return this.m_childId;     }
	
	public HAPStoryReferenceElement getConnectionRef() {    return this.m_connectionRef;     }
	
}
