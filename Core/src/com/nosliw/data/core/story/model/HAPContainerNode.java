package com.nosliw.data.core.story.model;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPStory;

public class HAPContainerNode {

	private HAPStory m_story;

	private String m_nodeId;
	
	private Object m_childId;
	
	private List<HAPContainerNode> m_children;
	
	public HAPContainerNode(String nodeId, HAPStory story) {
		this.m_story = story;
		this.m_nodeId = nodeId;
		this.m_children = new ArrayList<HAPContainerNode>();
	}

	
	public List<HAPContainerNode> getChildren(){   return this.m_children;    }
	public HAPContainerNode addChildNode(String nodeId, Object childId) {
		HAPContainerNode childNode = new HAPContainerNode(nodeId, m_story);
		this.m_children.add(childNode);
		childNode.setChildId(childId);
		return childNode;
	}
	
	public void setChildId(Object childId) {    this.m_childId = childId;    }
	public Object getChildId() {    return this.m_childId;    }
	
	public String getNodeId() {  return this.m_nodeId; 	}
	public HAPIdElement getStoryElementId() {    return new HAPIdElement(HAPConstant.STORYELEMENT_CATEGARY_NODE, this.m_nodeId);     }
	
}
