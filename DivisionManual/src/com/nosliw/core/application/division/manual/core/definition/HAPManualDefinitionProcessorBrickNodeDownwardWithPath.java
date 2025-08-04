package com.nosliw.core.application.division.manual.core.definition;

import com.nosliw.common.path.HAPPath;

public abstract class HAPManualDefinitionProcessorBrickNodeDownwardWithPath {

	public abstract boolean processBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data);

	public void postProcessBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data) {}

	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
}
