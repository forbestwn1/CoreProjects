package com.nosliw.uiresource.page.story.model;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPUtilityConnection;
import com.nosliw.data.core.story.design.HAPChangeInfo;
import com.nosliw.data.core.story.design.HAPChangeItem;
import com.nosliw.data.core.story.design.HAPUtilityChange;
import com.nosliw.data.core.story.element.connection.HAPConnectionContain;
import com.nosliw.uiresource.page.processor.HAPUtilityConfiguration;
import com.nosliw.uiresource.page.processor.HAPUtilityProcess;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUI;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUIData;
import com.nosliw.uiresource.page.story.element.HAPUIDataStructureInfo;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

public class HAPUINode {

	private HAPStory m_story;

	private String m_nodeId;
	
	private List<HAPUIChild> m_children;
	
	private List<HAPChangeItem> m_changes;
	
	private HAPStoryNodeUI m_storyNode;
	
	public HAPUINode(String nodeId, HAPStory story) {
		this.m_children = new ArrayList<HAPUIChild>();
		this.m_story = story;
		this.m_nodeId = nodeId;
	}

	public HAPUINode(HAPStoryNodeUI storyNode, HAPStory story) {
		this(storyNode.getId(), story);
		this.m_storyNode = storyNode;
	}

	public HAPUINode addChildNode(HAPStoryNodeUI childStoryNode, HAPConnectionContain connection) {
		HAPUIChild child = new HAPUIChild(HAPUtility.createUINodeFromStoryNode(childStoryNode), connection.getChildId(), connection.getId(), m_story);
		this.m_children.add(child);
		return child.getUINode();
	}
	
	public HAPUINode newChildNode(HAPStoryNodeUI childStoryNode, Object childId, HAPRequirementContextProcessor contextProcessRequirement, HAPUITagManager uiTagMan) {
		//build data info in ui node
		String nodeType = childStoryNode.getType();

		HAPConfigureContextProcessor contextProcessorConfig = HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(HAPConstant.UIRESOURCE_TYPE_TAG); 
		HAPUIDataStructureInfo dataStructureInfo = childStoryNode.getDataStructureInfo();
		HAPContextGroup currentContext = this.getStoryNode().getDataStructureInfo().getContext();
		HAPContextGroup childContext = null;
		if(HAPConstant.STORYNODE_TYPE_UIDATA.equals(nodeType)) {
			HAPStoryNodeUIData uiDataStoryNode = (HAPStoryNodeUIData)childStoryNode;
			childContext = HAPUtilityProcess.buildUITagContext(uiDataStoryNode.getTagName(), currentContext, uiDataStoryNode.getAttributes(), contextProcessorConfig, uiTagMan, contextProcessRequirement);
			dataStructureInfo.setContext(childContext);
		}
		else {
			childContext = HAPProcessorContext.processStatic(new HAPContextGroup(), HAPParentContext.createDefault(currentContext), contextProcessorConfig, contextProcessRequirement);
			dataStructureInfo.setContext(childContext);
		}
		
		//add node to story
		HAPChangeInfo newNodeChangeInfo = HAPUtilityChange.buildChangeNewAndApply(this.m_story, childStoryNode, this.m_changes);
		
		//connection
		HAPChangeInfo connectionNewChange = HAPUtilityChange.buildChangeNewAndApply(this.m_story, HAPUtilityConnection.newConnectionContain(this.m_nodeId, newNodeChangeInfo.getChangeItem().getId(), (String)childId), this.m_changes);

		HAPUIChild childNode = new HAPUIChild(newNodeChangeInfo.getStoryElement().getId(), childId, connectionNewChange.getStoryElement().getId(), this.m_story);
		return childNode.getUINode();
	}
	
	public HAPUIDataInfo getDataInfo(String name) {
		
	}
	
	private HAPStoryNodeUI getStoryNode() {
		if(this.m_storyNode==null) {
			this.m_story.getNode(this.m_nodeId);
		}
		return this.m_storyNode;
	}
	
	
	public List<HAPUIChild> getChildren(){   return this.m_children;    }
	
	public String getNodeId() {  return this.m_nodeId; 	}
	public HAPIdElement getStoryElementId() {    return new HAPIdElement(HAPConstant.STORYELEMENT_CATEGARY_NODE, this.m_nodeId);     }
	
}
