package com.nosliw.uiresource.page.story.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPReferenceElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPUtilityConnection;
import com.nosliw.data.core.story.change.HAPManagerChange;
import com.nosliw.data.core.story.change.HAPRequestChange;
import com.nosliw.data.core.story.change.HAPRequestChangeWrapper;
import com.nosliw.data.core.story.change.HAPUtilityChange;
import com.nosliw.data.core.story.element.connection.HAPConnectionContain;
import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPElementLeafData;
import com.nosliw.data.core.structure.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.HAPReferenceElement;
import com.nosliw.data.core.structure.HAPUtilityContext;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.uiresource.page.processor.HAPUtilityConfiguration;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUI;
import com.nosliw.uiresource.page.story.element.HAPUIDataStructureInfo;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;

public class HAPUINode {

	private HAPStory m_story;

	private HAPReferenceElement m_nodeRef;
	
	private List<HAPUIChild> m_children;
	
	private HAPStoryNodeUI m_storyNode;
	
	public HAPUINode(HAPReferenceElement nodeRef, HAPStory story) {
		this.m_children = new ArrayList<HAPUIChild>();
		this.m_story = story;
		this.m_nodeRef = nodeRef;
	}

	//this is for new story node
	public HAPUINode(HAPStoryNodeUI storyNode, HAPAliasElement alias, HAPStory story) {
		this(alias, story);
		this.m_storyNode = storyNode;
	}

	public HAPUINode getUINodeByStoryElementId(String storyEleId) {
		
		if(this.getStoryNode()!=null && this.getStoryNode().getElementId().getId().equals(storyEleId))  return this;

		for(HAPUIChild child : this.m_children) {
			HAPUINode out = child.getUINode().getUINodeByStoryElementId(storyEleId);
			if(out!=null)  return out;
		}
		return null;
	}

	public HAPUINode addChildNode(HAPStoryNodeUI childStoryNode, HAPConnectionContain connection) {
		HAPUIChild child = new HAPUIChild(new HAPUINode(childStoryNode.getElementId(), m_story), connection.getChildId(), connection.getElementId(), m_story);
		this.m_children.add(child);
		return child.getUINode();
	}
	
	public HAPUINode newChildNode(HAPStoryNodeUI childStoryNode, String alias, Object childId, HAPRequestChangeWrapper changeRequest, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan) {
		//build data info in ui node
		HAPUIDataStructureInfo dataStructureInfo = HAPUtility.buildDataStructureInfoForUIStoryNode(childStoryNode, this.getStoryNode().getDataStructureInfo().getContext(), runtimeEnv, uiTagMan);
		childStoryNode.setDataStructureInfo(dataStructureInfo);
		
		HAPAliasElement nodeName = changeRequest.addNewChange(childStoryNode, alias).getAlias();
		HAPAliasElement connectionName = changeRequest.addNewChange(HAPUtilityConnection.newConnectionContain(m_nodeRef, nodeName, (String)childId)).getAlias();
		
		HAPUIChild childNode = new HAPUIChild(childStoryNode, nodeName, childId, connectionName, this.m_story);
		this.m_children.add(childNode);
		return childNode.getUINode();
	}
	
	public HAPUIDataInfo getDataInfo(String name) {
		HAPUIDataInfo out = new HAPUIDataInfo();
		HAPInfoReferenceResolve resolve = HAPUtilityContext.resolveElementReference(new HAPReferenceElement(name), this.getStoryNode().getDataStructureInfo().getContext(), Arrays.asList(HAPValueStructureDefinitionGroup.getVisibleCategaries()).toArray(new String[0]), HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(HAPConstantShared.UIRESOURCE_TYPE_RESOURCE).elementReferenceResolveMode);
		HAPElement resolvedNode = resolve.resolvedElement;
		String nodeType = resolvedNode.getType();
		if(nodeType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
			HAPElementLeafData dataNode = (HAPElementLeafData)resolvedNode;
			out.setDataType(dataNode.getDataInfo());
			out.setContextPath(resolve.path);
		}
		return out;
	}
	
//	public HAPUINode getFirstDataUINode() {
//		HAPIdElement eleId = this.getStoryElementId();
//		HAPIdElementInfo idInfo = HAPUtilityStory.parseStoryElementId(eleId.getId());
//		if(idInfo.getType().equals(HAPConstant.STORYNODE_TYPE_UIDATA)) 	return this;
//		for(HAPUIChild child : this.getChildren()) {
//			HAPUINode firstDataUINode = child.getUINode().getFirstDataUINode();
//			if(firstDataUINode!=null)  return firstDataUINode;
//		}
//		return null;
//	}
	
	public List<HAPReferenceElement> getAllStoryElements(){
		List<HAPReferenceElement> out = new ArrayList<HAPReferenceElement>();
		out.add(this.getStoryNodeRef());
		for(HAPUIChild child : this.getChildren()) {
			out.add(child.getConnectionRef());
			out.addAll(child.getUINode().getAllStoryElements());
		}
		return out;
	}
	
	public List<HAPUINode> getAllUINodes(){
		List<HAPUINode> out = new ArrayList<HAPUINode>();
		out.add(this);
		for(HAPUIChild child : this.getChildren()) {
			out.addAll(child.getUINode().getAllUINodes());
		}
		return out;
	}
	
	public void updateUIDataStructureInfo(HAPUIDataStructureInfo parentDataStructureInfo, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan, HAPManagerChange changeMan) {
		HAPRequestChange changeRequest = this.m_story.newRequestChange(true);
		//update data structure
		HAPUIDataStructureInfo dataStructureInfo = buildDataStructureInfo(parentDataStructureInfo, runtimeEnv, uiTagMan);
		changeRequest.addChange(HAPUtilityChange.buildChangePatch(this.getStoryNodeRef(), HAPStoryNodeUI.DATASTRUCTURE, dataStructureInfo));
		this.getStory().change(changeRequest);
		
		for(HAPUIChild child : this.getChildren()) {
			child.getUINode().updateUIDataStructureInfo(dataStructureInfo, runtimeEnv, uiTagMan, changeMan);
		}
	}
	
	protected HAPUIDataStructureInfo buildDataStructureInfo(HAPUIDataStructureInfo parentDataStructureInfo, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan) {
		HAPUIDataStructureInfo out;
		if(parentDataStructureInfo==null) {
			//for page node
			out = HAPUtility.buildDataStructureInfoForPageNode(this.getStory());
		}
		else {
			//for ui node
			out = HAPUtility.buildDataStructureInfoForUIStoryNode(this.getStoryNode(), parentDataStructureInfo.getContext(), runtimeEnv, uiTagMan);
		}
		return out;
	}
	
	public HAPStoryNodeUI getStoryNode() {
		if(this.m_storyNode==null) {
			this.m_storyNode = (HAPStoryNodeUI)this.m_story.getElement(this.m_nodeRef);
		}
		return this.m_storyNode;
	}
	
	public List<HAPUIChild> getChildren(){   return this.m_children;    }
	public HAPReferenceElement getStoryNodeRef() {   return this.m_nodeRef;    }
	
//	public String getNodeId() {  return this.m_nodeId; 	}
//	public HAPIdElement getStoryElementId() {    return new HAPIdElement(HAPConstant.STORYELEMENT_CATEGARY_NODE, this.m_nodeId);     }
	
	protected HAPStory getStory() {    return this.m_story;     }
}
