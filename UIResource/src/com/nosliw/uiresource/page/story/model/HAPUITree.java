package com.nosliw.uiresource.page.story.model;

import com.nosliw.data.core.story.HAPStory;
import com.nosliw.uiresource.page.story.element.HAPStoryNodePage;

public class HAPUITree extends HAPUINode{

	public HAPUITree(String nodeId, HAPStory story) {
		super(nodeId, story);
	}

	public HAPUITree(HAPStoryNodePage storyNode, HAPStory story) {
		super(storyNode, story);
	}

/*	
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

	private void findNodeByType(HAPUINode node, String categary, String type, List<HAPIdElement> eles){
		String id = node.getStoryElementId().getId();
		HAPIdElementInfo idInfo = HAPUtilityStory.parseStoryElementId(id);
		if(HAPBasicUtility.isEquals(idInfo.getCategary(), categary) && HAPBasicUtility.isEquals(idInfo.getType(), type)) eles.add(node.getStoryElementId());
		for(HAPUINode child : node.getChildren()) {
			findNodeByType(child, categary, type, eles);
		}		
	}
	
	private void realize(HAPUINode node, List<HAPChangeItem> changesItem, List<HAPChangeInfo> changeInfos) {
		List<HAPUINode> children = node.getChildren();
		for(HAPUINode child : children) {
			HAPConnectionContain connection = HAPUtilityConnection.newConnectionContain(this.m_rootNode.getNodeId(), child.getNodeId(), (String)child.getChildId());
			this.m_connections.add(connection.getElementId());
			HAPChangeInfo changeInfo = HAPUtilityChange.buildChangeNewAndApply(this.m_story, connection, changesItem);
			changeInfos.add(changeInfo);
			realize(child, changesItem, changeInfos);
		}
	}
	
	private void discoverElements(HAPUINode rootNode, List<HAPIdElement> eles) {
		eles.add(rootNode.getStoryElementId());
		for(HAPUINode child : rootNode.getChildren()) {
			eles.add(child.getStoryElementId());
		}
	}
*/	
}
