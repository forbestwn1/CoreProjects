package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;

public abstract class HAPHandlerDownward {

	public abstract boolean processBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data);

	public abstract void postProcessBrickNode(HAPWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data);

	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
}
