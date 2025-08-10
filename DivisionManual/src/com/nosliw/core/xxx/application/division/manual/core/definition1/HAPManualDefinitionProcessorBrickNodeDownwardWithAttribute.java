package com.nosliw.core.xxx.application.division.manual.core.definition1;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionProcessorBrickNodeDownwardWithPath;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperBrickRoot;

public abstract class HAPManualDefinitionProcessorBrickNodeDownwardWithAttribute extends HAPManualDefinitionProcessorBrickNodeDownwardWithPath{

	@Override
	public boolean processBrickNode(HAPManualDefinitionWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data) {
		
	}

	@Override
	public void postProcessBrickNode(HAPManualDefinitionWrapperBrickRoot rootBrickWrapper, HAPPath path, Object data) {}

	@Override
	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
}
