package com.nosliw.core.xxx.application.division.manual.core.definition1;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionProcessorBrickNodeDownwardWithPath;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrick;

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
