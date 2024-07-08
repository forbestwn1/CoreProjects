package com.nosliw.core.application.division.manual.definition;

import com.nosliw.common.path.HAPPath;

public abstract class HAPManualDefinitionProcessorBrickNodeDownwardWithAttribute extends HAPManualDefinitionProcessorBrickNodeDownwardWithPath{

	@Override
	public boolean processBrickNode(HAPManualDefinitionWrapperBrick rootBrickWrapper, HAPPath path, Object data) {
		
	}

	@Override
	public void postProcessBrickNode(HAPManualDefinitionWrapperBrick rootBrickWrapper, HAPPath path, Object data) {}

	@Override
	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
}
