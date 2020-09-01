package com.nosliw.data.core.story.model;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPIdElementInfo;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPUtilityConnection;
import com.nosliw.data.core.story.HAPUtilityStory;
import com.nosliw.data.core.story.design.HAPChangeInfo;
import com.nosliw.data.core.story.design.HAPChangeItem;
import com.nosliw.data.core.story.design.HAPUtilityChange;
import com.nosliw.data.core.story.element.connection.HAPConnectionContain;

public class HAPContainerTree {

	private HAPStory m_story;
	
	private HAPContainerNode m_rootNode;
	
	private List<HAPIdElement> m_connections;
	
	public HAPContainerTree(HAPStory story, HAPContainerNode rootNode) {
		this.m_connections = new ArrayList<HAPIdElement>();
		this.m_story = story;
		this.m_rootNode = rootNode;
	}
	
	public List<HAPChangeInfo> realize(List<HAPChangeItem> changesItem){
		List<HAPChangeInfo> out = new ArrayList<HAPChangeInfo>();
		realize(this.m_rootNode, changesItem, out);
		return out;
	}
	
	public List<HAPIdElement> getAllElements(){
		List<HAPIdElement> out = new ArrayList<HAPIdElement>();
		this.discoverElements(this.m_rootNode, out);
		out.addAll(this.m_connections);
		return out;
	}
	
	public HAPIdElement getRootElementId() {    return this.m_rootNode.getStoryElementId();     }
	
	public List<HAPIdElement> searchNodeByType(String categary, String type){
		List<HAPIdElement> out = new ArrayList<HAPIdElement>();
		findNodeByType(this.m_rootNode, categary, type, out);
		return out;
	}

	private void findNodeByType(HAPContainerNode node, String categary, String type, List<HAPIdElement> eles){
		String id = node.getStoryElementId().getId();
		HAPIdElementInfo idInfo = HAPUtilityStory.parseStoryElementId(id);
		if(HAPBasicUtility.isEquals(idInfo.getCategary(), categary) && HAPBasicUtility.isEquals(idInfo.getType(), type)) eles.add(node.getStoryElementId());
		for(HAPContainerNode child : node.getChildren()) {
			findNodeByType(child, categary, type, eles);
		}		
	}
	
	private void realize(HAPContainerNode node, List<HAPChangeItem> changesItem, List<HAPChangeInfo> changeInfos) {
		List<HAPContainerNode> children = node.getChildren();
		for(HAPContainerNode child : children) {
			HAPConnectionContain connection = HAPUtilityConnection.newConnectionContain(this.m_rootNode.getNodeId(), child.getNodeId(), (String)child.getChildId());
			this.m_connections.add(connection.getElementId());
			HAPChangeInfo changeInfo = HAPUtilityChange.buildChangeNewAndApply(this.m_story, connection, changesItem);
			changeInfos.add(changeInfo);
			realize(child, changesItem, changeInfos);
		}
	}
	
	private void discoverElements(HAPContainerNode rootNode, List<HAPIdElement> eles) {
		eles.add(rootNode.getStoryElementId());
		for(HAPContainerNode child : rootNode.getChildren()) {
			eles.add(child.getStoryElementId());
		}
	}
}
