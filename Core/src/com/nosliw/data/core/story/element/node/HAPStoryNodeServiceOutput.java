package com.nosliw.data.core.story.element.node;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.story.HAPStoryNodeImp;

public class HAPStoryNodeServiceOutput extends HAPStoryNodeImp{

	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_SERVICEOUTPUT; 
	
	public HAPStoryNodeServiceOutput() {
		super(STORYNODE_TYPE);
	}

	@Override
	public boolean patch(String path, Object value) {
		if(!super.patch(path, value)) {
		}
		return false;
	}
}
