package com.nosliw.core.application.division.story.design.wizard.servicedriven;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.division.story.HAPStoryAliasElement;
import com.nosliw.core.application.division.story.HAPStoryReferenceElement;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.HAPStoryUtilityConnection;
import com.nosliw.core.application.division.story.brick.connection.HAPStoryConnectionContain;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUI;
import com.nosliw.core.application.division.story.change.HAPStoryManagerChange;
import com.nosliw.core.application.division.story.change.HAPStoryRequestChange;
import com.nosliw.core.application.division.story.change.HAPStoryRequestChangeWrapper;
import com.nosliw.core.application.division.story.change.HAPStoryUtilityChange;
import com.nosliw.core.application.uitag.HAPManagerUITag;
import com.nosliw.data.core.cronjob.HAPUtilityConfiguration;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPStoryUINode {

	private HAPStoryStory m_story;

	private HAPStoryReferenceElement m_nodeRef;
	
	private List<HAPStoryUIChild> m_children;
	
	private HAPStoryNodeUI m_storyNode;
	
	public HAPStoryUINode(HAPStoryReferenceElement nodeRef, HAPStoryStory story) {
		this.m_children = new ArrayList<HAPStoryUIChild>();
		this.m_story = story;
		this.m_nodeRef = nodeRef;
	}

	//this is for new story node
	public HAPStoryUINode(HAPStoryNodeUI storyNode, HAPStoryAliasElement alias, HAPStoryStory story) {
		this(alias, story);
		this.m_storyNode = storyNode;
	}

	public HAPStoryUINode getUINodeByStoryElementId(String storyEleId) {
		
		if(this.getStoryNode()!=null && this.getStoryNode().getElementId().getId().equals(storyEleId)) {
			return this;
		}

		for(HAPStoryUIChild child : this.m_children) {
			HAPStoryUINode out = child.getUINode().getUINodeByStoryElementId(storyEleId);
			if(out!=null) {
				return out;
			}
		}
		return null;
	}

	public HAPStoryUINode addChildNode(HAPStoryNodeUI childStoryNode, HAPStoryConnectionContain connection) {
		HAPStoryUIChild child = new HAPStoryUIChild(new HAPStoryUINode(childStoryNode.getElementId(), m_story), connection.getChildId(), connection.getElementId(), m_story);
		this.m_children.add(child);
		return child.getUINode();
	}
	
	public HAPStoryUINode newChildNode(HAPStoryNodeUI childStoryNode, String alias, Object childId, HAPStoryRequestChangeWrapper changeRequest, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan) {
		//build data info in ui node
		HAPStoryUIDataStructureInfo dataStructureInfo = HAPStoryUtility.buildDataStructureInfoForUIStoryNode(childStoryNode, this.getStoryNode().getDataStructureInfo().getContext(), runtimeEnv, uiTagMan);
		childStoryNode.setDataStructureInfo(dataStructureInfo);
		
		HAPStoryAliasElement nodeName = changeRequest.addNewChange(childStoryNode, alias).getAlias();
		HAPStoryAliasElement connectionName = changeRequest.addNewChange(HAPStoryUtilityConnection.newConnectionContain(m_nodeRef, nodeName, (String)childId)).getAlias();
		
		HAPStoryUIChild childNode = new HAPStoryUIChild(childStoryNode, nodeName, childId, connectionName, this.m_story);
		this.m_children.add(childNode);
		return childNode.getUINode();
	}
	
	public HAPStoryUIDataInfo getDataInfo(String name) {
		HAPStoryUIDataInfo out = new HAPStoryUIDataInfo();
		HAPResultReferenceResolve resolve = HAPUtilityStructure.resolveElementReference(name, this.getStoryNode().getDataStructureInfo().getContext(), HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(HAPConstantShared.UIRESOURCE_TYPE_RESOURCE).elementReferenceResolveMode, null);
		HAPElementStructure resolvedNode = resolve.finalElement;
		String nodeType = resolvedNode.getType();
		if(nodeType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
			HAPElementStructureLeafData dataNode = (HAPElementStructureLeafData)resolvedNode;
			out.setDataType(dataNode.getDataInfo());
			out.setIdPath(resolve.path);
			out.setRootReference(resolve.rootReference);
		}
		return out;
	}
	
//	public HAPStoryUINode getFirstDataUINode() {
//		HAPReferenceElement eleId = this.getStoryElementId();
//		HAPIdElementInfo idInfo = HAPUtilityStory.parseStoryElementId(eleId.getId());
//		if(idInfo.getType().equals(HAPConstant.STORYNODE_TYPE_UIDATA)) 	return this;
//		for(HAPStoryUIChild child : this.getChildren()) {
//			HAPStoryUINode firstDataUINode = child.getUINode().getFirstDataUINode();
//			if(firstDataUINode!=null)  return firstDataUINode;
//		}
//		return null;
//	}
	
	public List<HAPStoryReferenceElement> getAllStoryElements(){
		List<HAPStoryReferenceElement> out = new ArrayList<HAPStoryReferenceElement>();
		out.add(this.getStoryNodeRef());
		for(HAPStoryUIChild child : this.getChildren()) {
			out.add(child.getConnectionRef());
			out.addAll(child.getUINode().getAllStoryElements());
		}
		return out;
	}
	
	public List<HAPStoryUINode> getAllUINodes(){
		List<HAPStoryUINode> out = new ArrayList<HAPStoryUINode>();
		out.add(this);
		for(HAPStoryUIChild child : this.getChildren()) {
			out.addAll(child.getUINode().getAllUINodes());
		}
		return out;
	}
	
	public void updateUIDataStructureInfo(HAPStoryUIDataStructureInfo parentDataStructureInfo, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan, HAPStoryManagerChange changeMan) {
		HAPStoryRequestChange changeRequest = this.m_story.newRequestChange(true);
		//update data structure
		HAPStoryUIDataStructureInfo dataStructureInfo = buildDataStructureInfo(parentDataStructureInfo, runtimeEnv, uiTagMan);
		changeRequest.addChange(HAPStoryUtilityChange.buildChangePatch(this.getStoryNodeRef(), HAPStoryNodeUI.DATASTRUCTURE, dataStructureInfo));
		this.getStory().change(changeRequest);
		
		for(HAPStoryUIChild child : this.getChildren()) {
			child.getUINode().updateUIDataStructureInfo(dataStructureInfo, runtimeEnv, uiTagMan, changeMan);
		}
	}
	
	protected HAPStoryUIDataStructureInfo buildDataStructureInfo(HAPStoryUIDataStructureInfo parentDataStructureInfo, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan) {
		HAPStoryUIDataStructureInfo out;
		if(parentDataStructureInfo==null) {
			//for page node
			out = HAPStoryUtility.buildDataStructureInfoForPageNode(this.getStory());
		}
		else {
			//for ui node
			out = HAPStoryUtility.buildDataStructureInfoForUIStoryNode(this.getStoryNode(), parentDataStructureInfo.getContext(), runtimeEnv, uiTagMan);
		}
		return out;
	}
	
	public HAPStoryNodeUI getStoryNode() {
		if(this.m_storyNode==null) {
			this.m_storyNode = (HAPStoryNodeUI)this.m_story.getElement(this.m_nodeRef);
		}
		return this.m_storyNode;
	}
	
	public List<HAPStoryUIChild> getChildren(){   return this.m_children;    }
	public HAPStoryReferenceElement getStoryNodeRef() {   return this.m_nodeRef;    }
	
//	public String getNodeId() {  return this.m_nodeId; 	}
//	public HAPReferenceElement getStoryElementId() {    return new HAPReferenceElement(HAPConstant.STORYELEMENT_CATEGARY_NODE, this.m_nodeId);     }
	
	protected HAPStoryStory getStory() {    return this.m_story;     }
}
