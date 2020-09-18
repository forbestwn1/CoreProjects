package com.nosliw.uiresource.page.story.model;

import java.util.List;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.story.HAPInfoNodeChild;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPStoryNode;
import com.nosliw.data.core.story.HAPUtilityStory;
import com.nosliw.data.core.story.element.node.HAPStoryNodeVariable;
import com.nosliw.uiresource.page.processor.HAPUtilityConfiguration;
import com.nosliw.uiresource.page.processor.HAPUtilityProcess;
import com.nosliw.uiresource.page.story.element.HAPStoryNodePage;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUI;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUIData;
import com.nosliw.uiresource.page.story.element.HAPUIDataStructureInfo;
import com.nosliw.uiresource.page.tag.HAPUITagManager;

public class HAPUtility {

	//build data info in ui story node
	public static HAPUIDataStructureInfo buildDataStructureInfoForUIStoryNode(HAPStoryNodeUI uiStoryNode, HAPContextGroup parentContext, HAPRequirementContextProcessor contextProcessRequirement, HAPUITagManager uiTagMan) {
		HAPUIDataStructureInfo out = new HAPUIDataStructureInfo();

		String nodeType = uiStoryNode.getType();
		HAPConfigureContextProcessor contextProcessorConfig = HAPUtilityConfiguration.getContextProcessConfigurationForUIUit(HAPConstant.UIRESOURCE_TYPE_TAG); 
		HAPContextGroup childContext = null;
		if(HAPConstant.STORYNODE_TYPE_UIDATA.equals(nodeType)) {
			HAPStoryNodeUIData uiDataStoryNode = (HAPStoryNodeUIData)uiStoryNode;
			childContext = HAPUtilityProcess.buildUITagContext(uiDataStoryNode.getTagName(), parentContext, uiDataStoryNode.getAttributes(), contextProcessorConfig, uiTagMan, contextProcessRequirement);
			out.setContext(childContext);
		}
		else {
			childContext = HAPProcessorContext.processStatic(new HAPContextGroup(), HAPParentContext.createDefault(parentContext), contextProcessorConfig, contextProcessRequirement);
			out.setContext(childContext);
		}
		return out;
	}
	
	public static HAPUIDataStructureInfo buildDataStructureInfoForPageNode(HAPStory story) {
		HAPUIDataStructureInfo out = new HAPUIDataStructureInfo();
		Set<HAPStoryNode> varNodes = HAPUtilityStory.getStoryNodeByType(story, HAPConstant.STORYNODE_TYPE_VARIABLE);
		for(HAPStoryNode node : varNodes) {
			HAPStoryNodeVariable varNode = (HAPStoryNodeVariable)node;
			out.getContext().addPublicElement(varNode.getName(), new HAPContextDefinitionRoot(new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(varNode.getDataType()))));
		}
		return out;
	}

	public static HAPStoryNodePage buildPageStoryNode(HAPStory story) {
		HAPStoryNodePage out = new HAPStoryNodePage();
		HAPUIDataStructureInfo dataStructureInfo = buildDataStructureInfoForPageNode(story);
		out.setDataStructureInfo(dataStructureInfo);
		return out;
	}
	
	public static HAPUITree buildUITree(HAPStory story, HAPRequirementContextProcessor contextProcessRequirement, HAPUITagManager uiTagMan) {
		HAPStoryNodePage pageStoryNode = (HAPStoryNodePage)HAPUtilityStory.getAllStoryNodeByType(story, HAPConstant.STORYNODE_TYPE_PAGE).iterator().next();
		return (HAPUITree)buildUINode(pageStoryNode, story, contextProcessRequirement, uiTagMan);
	}

	private static HAPUINode buildUINode(HAPStoryNodeUI storyNode, HAPStory story, HAPRequirementContextProcessor contextProcessRequirement, HAPUITagManager uiTagMan) {
		HAPUINode out = null;

		String nodeType = storyNode.getType();
		switch(nodeType) {
		case HAPConstant.STORYNODE_TYPE_PAGE:
			out = new HAPUITree((HAPStoryNodePage)storyNode, story, contextProcessRequirement, uiTagMan); 
			break;
		default:
			out = new HAPUINode(storyNode, story); 
			break;
		}
		
		List<HAPInfoNodeChild> childrenNodeInfo = HAPUtilityStory.getAllChildNode(storyNode, story);
		for(HAPInfoNodeChild childNodeInfo : childrenNodeInfo) {
			if(childNodeInfo.getChildNode() instanceof HAPStoryNodeUI) {
				out.addChildNode((HAPStoryNodeUI)childNodeInfo.getChildNode(), childNodeInfo.getConnection());
			}
		}
		return out;
	}
}
