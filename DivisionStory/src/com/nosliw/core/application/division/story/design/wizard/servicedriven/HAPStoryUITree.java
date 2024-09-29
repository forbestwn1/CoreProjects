package com.nosliw.core.application.division.story.design.wizard.servicedriven;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.HAPStoryAliasElement;
import com.nosliw.core.application.division.story.HAPStoryIdElement;
import com.nosliw.core.application.division.story.HAPStoryIdElementInfo;
import com.nosliw.core.application.division.story.HAPStoryReferenceElement;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.HAPStoryUtilityStory;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodePage;
import com.nosliw.core.application.division.story.change.HAPStoryChangeItem;
import com.nosliw.core.application.division.story.change.HAPStoryHandlerChange;
import com.nosliw.core.application.division.story.change.HAPStoryManagerChange;
import com.nosliw.core.application.division.story.change.HAPStoryUtilityChange;
import com.nosliw.core.application.uitag.HAPManagerUITag;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPStoryUITree extends HAPStoryUINode implements HAPStoryHandlerChange{

	private HAPRuntimeEnvironment m_runtimeEnv;
	private HAPManagerUITag m_uiTagMan;
	private HAPStoryManagerChange m_changeMan;
	
	public HAPStoryUITree(HAPStoryReferenceElement nodeRef, HAPStoryStory story, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan, HAPStoryManagerChange changeMan) {
		super(nodeRef, story);
		this.m_runtimeEnv = runtimeEnv;
		this.m_uiTagMan = uiTagMan;
		this.m_changeMan = changeMan;
		init();
	}

	public HAPStoryUITree(HAPStoryNodePage storyNode, HAPStoryAliasElement alias, HAPStoryStory story, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan) {
		super(storyNode, alias, story);
		this.m_runtimeEnv = runtimeEnv;
		this.m_uiTagMan = uiTagMan;
		init();
	}
	
	private void init() {
		this.getStory().registerChangeHandler(this);
	}

	@Override
	public void onChanges(List<HAPStoryChangeItem> changes) {
		if(this.isDataRelatedChange(changes)) {
			this.updateUIDataStructureInfo(null, this.m_runtimeEnv, m_uiTagMan, this.m_changeMan);
		}
	}

	private boolean isDataRelatedChange(List<HAPStoryChangeItem> changes) {
		boolean out = false;
		for(HAPStoryChangeItem change : changes) {
			if(HAPStoryUtilityChange.isElementChange(change)) {
				HAPStoryIdElement targetEleId = HAPStoryUtilityChange.getChangeTargetElementId(change);
				if(HAPConstantShared.STORYELEMENT_CATEGARY_NODE.equals(targetEleId.getCategary())){
					String nodeId = targetEleId.getId();
					HAPStoryIdElementInfo idInfo = HAPStoryUtilityStory.parseStoryElementId(nodeId);
					String nodeType = idInfo.getType();
					if(nodeType.equals(HAPConstantShared.STORYNODE_TYPE_VARIABLE)) {
						return true;
					}
				}
			}
		}
		return out;
	}
	
/*	
	public List<HAPChangeInfo> realize(List<HAPChangeItem> changesItem){
		List<HAPChangeInfo> out = new ArrayList<HAPChangeInfo>();
		realize(this.m_rootNode, changesItem, out);
		return out;
	}
	
	public List<HAPReferenceElement> getAllElements(){
		List<HAPReferenceElement> out = new ArrayList<HAPReferenceElement>();
		this.discoverElements(this.m_rootNode, out);
		out.addAll(this.m_connections);
		return out;
	}
	
	public HAPReferenceElement getRootElementId() {    return this.m_rootNode.getStoryElementId();     }
	
	public List<HAPReferenceElement> searchNodeByType(String categary, String type){
		List<HAPReferenceElement> out = new ArrayList<HAPReferenceElement>();
		findNodeByType(this.m_rootNode, categary, type, out);
		return out;
	}

	private void findNodeByType(HAPStoryUINode node, String categary, String type, List<HAPReferenceElement> eles){
		String id = node.getStoryElementId().getId();
		HAPIdElementInfo idInfo = HAPUtilityStory.parseStoryElementId(id);
		if(HAPBasicUtility.isEquals(idInfo.getCategary(), categary) && HAPBasicUtility.isEquals(idInfo.getType(), type)) eles.add(node.getStoryElementId());
		for(HAPStoryUINode child : node.getChildren()) {
			findNodeByType(child, categary, type, eles);
		}		
	}
	
	private void realize(HAPStoryUINode node, List<HAPChangeItem> changesItem, List<HAPChangeInfo> changeInfos) {
		List<HAPStoryUINode> children = node.getChildren();
		for(HAPStoryUINode child : children) {
			HAPConnectionContain connection = HAPUtilityConnection.newConnectionContain(this.m_rootNode.getNodeId(), child.getNodeId(), (String)child.getChildId());
			this.m_connections.add(connection.getElementId());
			HAPChangeInfo changeInfo = HAPUtilityChange.buildChangeNewAndApply(this.m_story, connection, changesItem);
			changeInfos.add(changeInfo);
			realize(child, changesItem, changeInfos);
		}
	}
	
	private void discoverElements(HAPStoryUINode rootNode, List<HAPReferenceElement> eles) {
		eles.add(rootNode.getStoryElementId());
		for(HAPStoryUINode child : rootNode.getChildren()) {
			eles.add(child.getStoryElementId());
		}
	}
*/	
}
