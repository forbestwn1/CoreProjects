package com.nosliw.core.application.division.manual.definition;

import com.nosliw.common.path.HAPPath;

public abstract class HAPManualDefinitionProcessorBrickNodeDownwardWithPath {

	public abstract boolean processBrickNode(HAPManualDefinitionWrapperBrick rootBrickWrapper, HAPPath path, Object data);

	public void postProcessBrickNode(HAPManualDefinitionWrapperBrick rootBrickWrapper, HAPPath path, Object data) {}

	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
}
