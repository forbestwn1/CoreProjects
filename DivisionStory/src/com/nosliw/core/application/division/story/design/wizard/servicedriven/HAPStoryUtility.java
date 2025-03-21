package com.nosliw.core.application.division.story.design.wizard.servicedriven;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.HAPStoryInfoNodeChild;
import com.nosliw.core.application.division.story.HAPStoryStory;
import com.nosliw.core.application.division.story.HAPStoryUtilityStory;
import com.nosliw.core.application.division.story.brick.HAPStoryNode;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUI;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeUIPage;
import com.nosliw.core.application.division.story.brick.node.HAPStoryNodeVariable;
import com.nosliw.core.application.division.story.change.HAPStoryManagerChange;
import com.nosliw.core.application.uitag.HAPManagerUITag;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPStoryUtility {

	public static List<HAPStoryNodeVariable> getAllChildVariableNodes(HAPStoryNode parentNode, HAPStoryStory story){
		List<HAPStoryInfoNodeChild> childNodeInfo = HAPStoryUtilityStory.getAllChildNodeByType(parentNode, HAPStoryNodeVariable.STORYNODE_TYPE, story);
		return childNodeInfo.stream().map(info->(HAPStoryNodeVariable)info.getChildNode()).collect(Collectors.toList());
	}
	
	//build data info in ui story node
	public static HAPStoryUIDataStructureInfo buildDataStructureInfoForUIStoryNode(HAPStoryNodeUI uiStoryNode, HAPValueStructureDefinitionGroup parentContext, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan) {
		HAPStoryUIDataStructureInfo out = new HAPStoryUIDataStructureInfo();

//		String nodeType = uiStoryNode.getType();
//		HAPConfigureProcessorValueStructure contextProcessorConfig = HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(HAPConstantShared.UIRESOURCE_TYPE_TAG); 
//		HAPValueStructureDefinitionGroup childContext = null;
//		if(HAPConstantShared.STORYNODE_TYPE_UITAGDATA.equals(nodeType)) {
//			HAPStoryNodeUITagData uiDataStoryNode = (HAPStoryNodeUITagData)uiStoryNode;
//			childContext = HAPUtilityProcess.buildUITagValueStructure(uiTagMan.getUITagDefinition(new HAPUITagId(uiDataStoryNode.getTagName())), parentContext, uiDataStoryNode.getAttributes(), contextProcessorConfig, runtimeEnv);
//			childContext = (HAPValueStructureDefinitionGroup)HAPProcessorStructure.processRelative(childContext, HAPContainerStructure.createDefault(parentContext), null, contextProcessorConfig, runtimeEnv);
//			out.setContext(childContext);
//		}
//		else {
//			childContext = (HAPValueStructureDefinitionGroup)HAPProcessorStructure.processStatic(new HAPValueStructureDefinitionGroup(), HAPContainerStructure.createDefault(parentContext), null, null, contextProcessorConfig, runtimeEnv);
//			childContext = (HAPValueStructureDefinitionGroup)HAPProcessorStructure.processRelative(childContext, HAPContainerStructure.createDefault(parentContext), null, contextProcessorConfig, runtimeEnv);
//			out.setContext(childContext);
//		}
		return out;
	}
	
	public static HAPStoryUIDataStructureInfo buildDataStructureInfoForPageNode(HAPStoryStory story) {
		HAPStoryUIDataStructureInfo out = new HAPStoryUIDataStructureInfo();
		Set<HAPStoryNode> varNodes = HAPStoryUtilityStory.getAllStoryNodeByType(story, HAPConstantShared.STORYNODE_TYPE_VARIABLE);
		for(HAPStoryNode node : varNodes) {
			HAPStoryNodeVariable varNode = (HAPStoryNodeVariable)node;
//			HAPRootStructure varRoot = new HAPRootStructure(new HAPElementStructureLeafData(varNode.getVariableInfo().getDataInfo()));
//			varRoot.setId(varNode.getId());
//			varRoot.setName(varNode.getVariableInfo().getName());
//			varRoot.setLocalId(varNode.getId());
//			out.getContext().addRootToCategary(null, varRoot);
		}
		return out;
	}

	public static HAPStoryNodeUIPage buildPageStoryNode(HAPStoryStory story) {
		HAPStoryNodeUIPage out = new HAPStoryNodeUIPage();
		HAPStoryUIDataStructureInfo dataStructureInfo = buildDataStructureInfoForPageNode(story);
		out.setDataStructureInfo(dataStructureInfo);
		return out;
	}
	
	public static HAPStoryUIPage buildUITree(HAPStoryStory story, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan, HAPStoryManagerChange changeMan) {
		HAPStoryNodeUIPage pageStoryNode = (HAPStoryNodeUIPage)HAPStoryUtilityStory.getAllStoryNodeByType(story, HAPConstantShared.STORYNODE_TYPE_PAGE).iterator().next();
		HAPStoryUINode uiNode = createUINodeByStoryNode(pageStoryNode, story, runtimeEnv, uiTagMan, changeMan);
		buildChildUINode(uiNode, story, runtimeEnv, uiTagMan, changeMan);
		return (HAPStoryUIPage)uiNode;
	}

	private static HAPStoryUINode createUINodeByStoryNode(HAPStoryNodeUI storyNode, HAPStoryStory story, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan, HAPStoryManagerChange changeMan) {
		HAPStoryUINode out = null;

		String nodeType = storyNode.getType();
		switch(nodeType) {
		case HAPConstantShared.STORYNODE_TYPE_PAGE:
			out = new HAPStoryUIPage(storyNode.getElementId(), story, runtimeEnv, uiTagMan, changeMan); 
			break;
		default:
			out = new HAPStoryUINode(storyNode.getElementId(), story); 
			break;
		}
		return out;
	}
	
	private static void buildChildUINode(HAPStoryUINode uiNode, HAPStoryStory story, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan, HAPStoryManagerChange changeMan) {
		List<HAPStoryInfoNodeChild> childrenNodeInfo = HAPStoryUtilityStory.getAllChildNode(uiNode.getStoryNode(), story);
		for(HAPStoryInfoNodeChild childNodeInfo : childrenNodeInfo) {
			if(childNodeInfo.getChildNode() instanceof HAPStoryNodeUI) {
				HAPStoryUINode childUINode = uiNode.addChildNode((HAPStoryNodeUI)childNodeInfo.getChildNode(), childNodeInfo.getConnection());
				buildChildUINode(childUINode, story, runtimeEnv, uiTagMan, changeMan);
			}
		}
	}
}
