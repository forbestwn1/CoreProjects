package com.nosliw.core.application.division.story.brick.node;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.story.brick.HAPStoryElement;
import com.nosliw.core.application.division.story.change.HAPStoryChangeResult;
import com.nosliw.core.application.division.story.xxx.brick.HAPStoryNodeImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPStoryNodeServiceOutput extends HAPStoryNodeImp{

	public final static String STORYNODE_TYPE = HAPConstantShared.STORYNODE_TYPE_SERVICEOUTPUT; 
	
	public HAPStoryNodeServiceOutput() {
		super(STORYNODE_TYPE);
		this.setEmptyDisplayName();
	}

	@Override
	public HAPStoryChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		return super.patch(path, value, runtimeEnv);
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeServiceOutput out = new HAPStoryNodeServiceOutput();
		super.cloneTo(out);
		return out;
	}
}
