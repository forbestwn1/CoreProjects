package com.nosliw.data.core.story.element.node;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.story.HAPStoryElement;
import com.nosliw.data.core.story.HAPStoryNodeImp;
import com.nosliw.data.core.story.change.HAPChangeResult;

public class HAPStoryNodeServiceInput extends HAPStoryNodeImp{

	public final static String STORYNODE_TYPE = HAPConstant.STORYNODE_TYPE_SERVICEINPUT; 
	
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
	public HAPChangeResult patch(String path, Object value, HAPRuntimeEnvironment runtimeEnv) {
		return super.patch(path, value, runtimeEnv);
	}
}
