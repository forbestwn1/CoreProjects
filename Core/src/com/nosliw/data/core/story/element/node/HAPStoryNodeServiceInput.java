package com.nosliw.data.core.story.element.node;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.design.HAPChangeResult;

public class HAPStoryNodeServiceInput extends HAPStoryNodeImp{

	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_SERVICEINPUT; 
	
	public HAPStoryNodeServiceInput() {
		super(STORYNODE_TYPE);
	}

	@Override
	public HAPChangeResult patch(String path, Object value) {
		return super.patch(path, value);
	}
}
