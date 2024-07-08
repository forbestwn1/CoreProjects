package com.nosliw.core.application.division.manual;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockComplex;

public abstract class HAPPluginProcessorBlockComplexImp implements HAPPluginProcessorBlockComplex{

	private HAPIdBrickType m_brickTypeId;
	
	public HAPPluginProcessorBlockComplexImp(HAPIdBrickType entityType) {
		this.m_brickTypeId = entityType;
	}

	@Override
	public HAPIdBrickType getBrickType() {    return this.m_brickTypeId;    }

	//process definition before value context
	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	@Override
	public void postProcessInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	@Override
	public void postProcessBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}


	
	
	
	
	@Override
	public void extendConstantValue(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	

	
	
	//value context extension, variable resolve
	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	@Override
	public void postProcessVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	
	//matcher
	@Override
	public void processValueContextDiscovery(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	@Override
	public void postProcessValueContextDiscovery(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	
	@Override
	public void processEntity(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {	}
	@Override
	public void postProcessEntity(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {	}


	protected Pair<HAPManualDefinitionBrick, HAPManualBrick> getBrickPair(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext){
		return HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, processContext.getCurrentBundle());
	}
}
