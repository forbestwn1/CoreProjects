package com.nosliw.uiresource.page.story.model;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPStory;

public class HAPUIChild {

	private HAPStory m_story;

	private HAPUINode m_node;
	
	private Object m_childId;

	private String m_connectionId;

	public HAPUIChild(HAPUINode node, Object childId, String connectionId, HAPStory story) {
		this.m_node = node;
		this.m_childId = childId;
		this.m_connectionId = connectionId;
		this.m_story = story;
	}

	public HAPUIChild(String storyNodeId, Object childId, String connectionId, HAPStory story) {
		this(new HAPUINode(storyNodeId, story), childId, connectionId, story);
	}

	public HAPUINode getUINode() {    return this.m_node;     }
	
	public Object getChildId() {    return this.m_childId;     }
	
	public String getConnectionId() {    return this.m_connectionId;     }
	
	public HAPIdElement getConnectionElementId() {    return new HAPIdElement(HAPConstant.STORYELEMENT_CATEGARY_CONNECTION, this.m_connectionId);     }

}
