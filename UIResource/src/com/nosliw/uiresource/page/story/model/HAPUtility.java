package com.nosliw.uiresource.page.story.model;

import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPInfoNodeChild;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryNode;
import com.nosliw.data.core.story.HAPUtilityStory;
import com.nosliw.data.core.story.change.HAPManagerChange;
import com.nosliw.data.core.story.element.node.HAPStoryNodeVariable;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPElementLeafData;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.temp.HAPProcessorContext;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionGroup;
import com.nosliw.uiresource.page.processor.HAPUtilityConfiguration;
import com.nosliw.uiresource.page.processor.HAPUtilityProcess;
import com.nosliw.uiresource.page.story.element.HAPStoryNodePage;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUI;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUIData;
import com.nosliw.uiresource.page.story.element.HAPUIDataStructureInfo;
import com.nosliw.uiresource.page.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.tag.HAPUITagId;

public class HAPUtility {

	//build data info in ui story node
	public static HAPUIDataStructureInfo buildDataStructureInfoForUIStoryNode(HAPStoryNodeUI uiStoryNode, HAPValueStructureDefinitionGroup parentContext, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan) {
		HAPUIDataStructureInfo out = new HAPUIDataStructureInfo();

		String nodeType = uiStoryNode.getType();
		HAPConfigureProcessorStructure contextProcessorConfig = HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(HAPConstantShared.UIRESOURCE_TYPE_TAG); 
		HAPValueStructureDefinitionGroup childContext = null;
		if(HAPConstantShared.STORYNODE_TYPE_UIDATA.equals(nodeType)) {
			HAPStoryNodeUIData uiDataStoryNode = (HAPStoryNodeUIData)uiStoryNode;
			childContext = HAPUtilityProcess.buildUITagContext(uiTagMan.getUITagDefinition(new HAPUITagId(uiDataStoryNode.getTagName())), parentContext, uiDataStoryNode.getAttributes(), contextProcessorConfig, runtimeEnv);
			childContext = HAPProcessorContext.processRelative(childContext, HAPContainerStructure.createDefault(parentContext), contextProcessorConfig, runtimeEnv);
			out.setContext(childContext);
		}
		else {
			childContext = HAPProcessorContext.processStatic(new HAPValueStructureDefinitionGroup(), HAPContainerStructure.createDefault(parentContext), contextProcessorConfig, runtimeEnv);
			childContext = HAPProcessorContext.processRelative(childContext, HAPContainerStructure.createDefault(parentContext), contextProcessorConfig, runtimeEnv);
			out.setContext(childContext);
		}
		return out;
	}
	
	public static HAPUIDataStructureInfo buildDataStructureInfoForPageNode(HAPStory story) {
		HAPUIDataStructureInfo out = new HAPUIDataStructureInfo();
		Set<HAPStoryNode> varNodes = HAPUtilityStory.getAllStoryNodeByType(story, HAPConstantShared.STORYNODE_TYPE_VARIABLE);
		for(HAPStoryNode node : varNodes) {
			HAPStoryNodeVariable varNode = (HAPStoryNodeVariable)node;
			HAPRoot varRoot = new HAPRoot(new HAPElementLeafData(varNode.getVariableInfo().getDataInfo()));
			varRoot.setId(varNode.getId());
			out.getContext().addPublicElement(varNode.getVariableInfo().getName(), varRoot);
		}
		return out;
	}

	public static HAPStoryNodePage buildPageStoryNode(HAPStory story) {
		HAPStoryNodePage out = new HAPStoryNodePage();
		HAPUIDataStructureInfo dataStructureInfo = buildDataStructureInfoForPageNode(story);
		out.setDataStructureInfo(dataStructureInfo);
		return out;
	}
	
	public static HAPUITree buildUITree(HAPStory story, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan, HAPManagerChange changeMan) {
		HAPStoryNodePage pageStoryNode = (HAPStoryNodePage)HAPUtilityStory.getAllStoryNodeByType(story, HAPConstantShared.STORYNODE_TYPE_PAGE).iterator().next();
		HAPUINode uiNode = createUINodeByStoryNode(pageStoryNode, story, runtimeEnv, uiTagMan, changeMan);
		buildChildUINode(uiNode, story, runtimeEnv, uiTagMan, changeMan);
		return (HAPUITree)uiNode;
	}

	private static HAPUINode createUINodeByStoryNode(HAPStoryNodeUI storyNode, HAPStory story, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan, HAPManagerChange changeMan) {
		HAPUINode out = null;

		String nodeType = storyNode.getType();
		switch(nodeType) {
		case HAPConstantShared.STORYNODE_TYPE_PAGE:
			out = new HAPUITree(storyNode.getElementId(), story, runtimeEnv, uiTagMan, changeMan); 
			break;
		default:
			out = new HAPUINode(storyNode.getElementId(), story); 
			break;
		}
		return out;
	}
	
	private static void buildChildUINode(HAPUINode uiNode, HAPStory story, HAPRuntimeEnvironment runtimeEnv, HAPManagerUITag uiTagMan, HAPManagerChange changeMan) {
		List<HAPInfoNodeChild> childrenNodeInfo = HAPUtilityStory.getAllChildNode(uiNode.getStoryNode(), story);
		for(HAPInfoNodeChild childNodeInfo : childrenNodeInfo) {
			if(childNodeInfo.getChildNode() instanceof HAPStoryNodeUI) {
				HAPUINode childUINode = uiNode.addChildNode((HAPStoryNodeUI)childNodeInfo.getChildNode(), childNodeInfo.getConnection());
				buildChildUINode(childUINode, story, runtimeEnv, uiTagMan, changeMan);
			}
		}
	}
}
