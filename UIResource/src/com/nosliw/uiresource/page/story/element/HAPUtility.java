package com.nosliw.uiresource.page.story.element;

import com.nosliw.data.core.story.HAPParserElement;

public class HAPUtility {

	static {
		HAPParserElement.registerStoryNode(HAPStoryNodePage.STORYNODE_TYPE, HAPStoryNodePage.class);
		HAPParserElement.registerStoryNode(HAPStoryNodeUIData.STORYNODE_TYPE, HAPStoryNodeUIData.class);
		HAPParserElement.registerStoryNode(HAPStoryNodeUIHtml.STORYNODE_TYPE, HAPStoryNodeUIHtml.class);
	}
	
}
