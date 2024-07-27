package com.nosliw.core.application.division.manual.definition;

import com.nosliw.common.path.HAPPath;

public abstract class HAPManualDefinitionProcessorBrickNodeDownwardWithBrick extends HAPManualDefinitionProcessorBrickNodeDownwardWithPath{

	@Override
	public boolean processBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data) {
		return this.processBrick(HAPManualDefinitionUtilityBrick.getDescendantBrickDefinition(rootBrick, path), data);
	}

	@Override
	public void postProcessBrickNode(HAPManualDefinitionBrick rootBrick, HAPPath path, Object data) {
		this.postProcessBrick(HAPManualDefinitionUtilityBrick.getDescendantBrickDefinition(rootBrick, path), data);
	}

	abstract protected boolean processBrick(HAPManualDefinitionBrick brick, Object data); 
	
	protected void postProcessBrick(HAPManualDefinitionBrick brick, Object data) {} 
	
	@Override
	protected boolean isRoot(HAPPath path) {
		return path==null||path.isEmpty();
	}
	
}
