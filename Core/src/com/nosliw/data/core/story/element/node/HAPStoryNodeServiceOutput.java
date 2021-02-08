package com.nosliw.data.core.story.element.node;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.change.HAPChangeResult;

public class HAPStoryNodeServiceOutput extends HAPStoryNodeImp{

	public final static String STORYNODE_TYPE = HAPConstantShared.STORYNODE_TYPE_SERVICEOUTPUT; 
	
	public HAPStoryNodeServiceOutput() {
		super(STORYNODE_TYPE);
		this.setEmptyDisplayName();
	}

	@Override
	public HAPChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		return super.patch(path, value, runtimeEnv);
	}

	@Override
	public HAPStoryElement cloneStoryElement() {
		HAPStoryNodeServiceOutput out = new HAPStoryNodeServiceOutput();
		super.cloneTo(out);
		return out;
	}
}
