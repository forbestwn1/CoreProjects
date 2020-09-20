package com.nosliw.uiresource.page.story.model;

import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPReferenceElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUI;

public class HAPUIChild {

	private HAPStory m_story;

	private HAPUINode m_node;
	
	private Object m_childId;

	private HAPReferenceElement m_connectionRef;

	public HAPUIChild(HAPUINode node, Object childId, HAPReferenceElement connectionRef, HAPStory story) {
		this.m_node = node;
		this.m_childId = childId;
		this.m_connectionRef = connectionRef;
		this.m_story = story;
	}

	//for new node
	public HAPUIChild(HAPStoryNodeUI storyNode, HAPAliasElement alias, Object childId, HAPReferenceElement connectionRef, HAPStory story) {
		this(new HAPUINode(storyNode, alias, story), childId, connectionRef, story);
	}

	//for solid node
	public HAPUIChild(HAPIdElement storyNodeId, Object childId, HAPReferenceElement connectionRef, HAPStory story) {
		this(new HAPUINode(storyNodeId, story), childId, connectionRef, story);
	}

	public HAPUINode getUINode() {    return this.m_node;     }
	
	public Object getChildId() {    return this.m_childId;     }
	
	public HAPReferenceElement getConnectionRef() {    return this.m_connectionRef;     }
	
}
