package com.nosliw.data.core.story.element.node;

import java.util.List;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.design.HAPChangeItem;

public class HAPStoryNodeServiceInput extends HAPStoryNodeImp{

	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_SERVICEINPUT; 
	
	public HAPStoryNodeServiceInput() {
		super(STORYNODE_TYPE);
	}

	@Override
	public List<HAPChangeItem> patch(String path, Object value) {
		return super.patch(path, value);
	}
}
