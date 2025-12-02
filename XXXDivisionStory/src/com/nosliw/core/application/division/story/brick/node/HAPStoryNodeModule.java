package com.nosliw.core.application.division.story.brick.node;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.xxx.brick.HAPStoryNodeImp;

public class HAPStoryNodeModule extends HAPStoryNodeImp{

	public final static String STORYNODE_TYPE = HAPConstantShared.STORYNODE_TYPE_MODULE;

	public HAPStoryNodeModule() {
		super(STORYNODE_TYPE);
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeModule out = new HAPStoryNodeModule();
		super.cloneTo(out);
		return out;

	} 
}
