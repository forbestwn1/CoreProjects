package com.nosliw.uiresource.page.story.element;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.story.HAPStoryElement;

public class HAPStoryNodeUITagOther extends HAPStoryNodeUITag{

	public final static String STORYNODE_TYPE = HAPConstantShared.STORYNODE_TYPE_UITAGOTHER; 
	

	public HAPStoryNodeUITagOther() {}

	public HAPStoryNodeUITagOther(String tagName, String id) {
		super(STORYNODE_TYPE, tagName, id);
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeUITagOther out = new HAPStoryNodeUITagOther();
		this.cloneToUITagStoryNode(out);
		return out;
	}

}
