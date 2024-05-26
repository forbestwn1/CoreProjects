package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;

public abstract class HAPManualProcessorBrickNodeDownwardWithAttribute extends HAPManualProcessorBrickNodeDownwardWithPath{

	@Override
	public boolean processBrickNode(HAPManualWrapperBrick rootBrickWrapper, HAPPath path, Object data) {
		
	}

	@Override
	public void postProcessBrickNode(HAPManualWrapperBrick rootBrickWrapper, HAPPath path, Object data) {}

	@Override
	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
}
