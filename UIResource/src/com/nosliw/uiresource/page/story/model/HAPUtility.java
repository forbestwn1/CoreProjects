package com.nosliw.uiresource.page.story.model;

import java.util.List;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPInfoNodeChild;
import com.nosliw.data.core.story.HAPStory;
import com.nosliw.data.core.story.HAPUtilityStory;
import com.nosliw.uiresource.page.story.element.HAPStoryNodePage;
import com.nosliw.uiresource.page.story.element.HAPStoryNodeUI;

public class HAPUtility {

	public static HAPStoryNodePage buildPageStoryNode(HAPStory story) {
		
	}
	
	public static HAPUITree buildUITree(HAPStory story) {
		HAPStoryNodePage pageStoryNode = (HAPStoryNodePage)HAPUtilityStory.getAllStoryNodeByType(story, HAPConstant.STORYNODE_TYPE_PAGE).iterator().next();
		return (HAPUITree)buildUINode(pageStoryNode, story);
	}

	public static HAPUINode createUINodeFromStoryNode(HAPStoryNodeUI storyNode, HAPStory story) {
		HAPUINode out = null;
		String nodeType = storyNode.getType();
		switch(nodeType) {
		case HAPConstant.STORYNODE_TYPE_PAGE:
			out = new HAPUITree((HAPStoryNodePage)storyNode, story); 
			break;
		default:
			out = new HAPUINode(storyNode, story); 
			break;
		}
		return out;
	}
	
	private static HAPUINode buildUINode(HAPStoryNodeUI storyNode, HAPStory story) {
		HAPUINode out = createUINodeFromStoryNode(storyNode, story);
		
		List<HAPInfoNodeChild> childrenNodeInfo = HAPUtilityStory.getAllChildNode(storyNode, story);
		for(HAPInfoNodeChild childNodeInfo : childrenNodeInfo) {
			if(childNodeInfo.getChildNode() instanceof HAPStoryNodeUI) {
				out.addChildNode((HAPStoryNodeUI)childNodeInfo.getChildNode(), childNodeInfo.getConnection());
			}
		}
		return out;
	}
	
}
