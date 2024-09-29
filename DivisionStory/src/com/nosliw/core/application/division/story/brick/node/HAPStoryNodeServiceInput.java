package com.nosliw.core.application.division.story.brick.node;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.brick.HAPStoryNodeImp;
import com.nosliw.core.application.division.story.change.HAPStoryChangeResult;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPStoryNodeServiceInput extends HAPStoryNodeImp{

	public final static String STORYNODE_TYPE = HAPConstantShared.STORYNODE_TYPE_SERVICEINPUT; 
	
	public HAPStoryNodeServiceInput() {
		super(STORYNODE_TYPE);
		this.setEmptyDisplayName();
	}
	
	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeServiceInput out = new HAPStoryNodeServiceInput();
		super.cloneTo(out);
		return out;
	}

	@Override
	public HAPStoryChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		return super.patch(path, value, runtimeEnv);
	}
}
