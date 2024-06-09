package com.nosliw.uiresource.page.story.model;

import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPAliasElement;
import com.nosliw.data.core.story.HAPIdElement;
import com.nosliw.data.core.story.HAPIdElementInfo;
import com.nosliw.data.core.story.HAPReferenceElement;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPUtilityStory;
import com.nosliw.data.core.story.change.HAPChangeItem;
import com.nosliw.data.core.story.change.HAPHandlerChange;
import com.nosliw.data.core.story.change.HAPManagerChange;
import com.nosliw.data.core.story.change.HAPUtilityChange;
import com.nosliw.ui.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.story.element.HAPStoryNodePage;

public class HAPUITree extends HAPUINode implements HAPHandlerChange{

	private HAPRuntimeEnvironment m_runtimeEnv;
	private HAPManagerUITag m_uiTagMan;
	private HAPManagerChange m_changeMan;
	
	public HAPUITree(HAPReferenceElement nodeRef, HAPStory story, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan, HAPManagerChange changeMan) {
		super(nodeRef, story);
		this.m_runtimeEnv = runtimeEnv;
		this.m_uiTagMan = uiTagMan;
		this.m_changeMan = changeMan;
		init();
	}

	public HAPUITree(HAPStoryNodePage storyNode, HAPAliasElement alias, HAPStory story, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan) {
		super(storyNode, alias, story);
		this.m_runtimeEnv = runtimeEnv;
		this.m_uiTagMan = uiTagMan;
		init();
	}
	
	private void init() {
		this.getStory().registerChangeHandler(this);
	}

	@Override
	public void onChanges(List<HAPChangeItem> changes) {
		if(this.isDataRelatedChange(changes)) {
			this.updateUIDataStructureInfo(null, this.m_runtimeEnv, m_uiTagMan, this.m_changeMan);
		}
	}

	private boolean isDataRelatedChange(List<HAPChangeItem> changes) {
		boolean out = false;
		for(HAPChangeItem change : changes) {
			if(HAPUtilityChange.isElementChange(change)) {
				HAPIdElement targetEleId = HAPUtilityChange.getChangeTargetElementId(change);
				if(HAPConstantShared.STORYELEMENT_CATEGARY_NODE.equals(targetEleId.getCategary())){
					String nodeId = targetEleId.getId();
					HAPIdElementInfo idInfo = HAPUtilityStory.parseStoryElementId(nodeId);
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

	private void findNodeByType(HAPUINode node, String categary, String type, List<HAPReferenceElement> eles){
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
	
	private void discoverElements(HAPUINode rootNode, List<HAPReferenceElement> eles) {
		eles.add(rootNode.getStoryElementId());
		for(HAPUINode child : rootNode.getChildren()) {
			eles.add(child.getStoryElementId());
		}
	}
*/	
}
