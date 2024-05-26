package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;

public abstract class HAPManualProcessorBrickNodeDownwardWithPath {

	public abstract boolean processBrickNode(HAPManualWrapperBrick rootBrickWrapper, HAPPath path, Object data);

	public void postProcessBrickNode(HAPManualWrapperBrick rootBrickWrapper, HAPPath path, Object data) {}

	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
}
