package com.nosliw.core.application.division.manual;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrick;
import com.nosliw.core.application.HAPBrickBlockComplex;
import com.nosliw.core.application.HAPIdBrickType;

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
	public void extendConstantValue(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	

	
	
	//value context extension, variable resolve
	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	@Override
	public void postProcessVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	
	//matcher
	@Override
	public void processValueContextDiscovery(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	@Override
	public void postProcessValueContextDiscovery(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	
	@Override
	public void processEntity(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {	}
	@Override
	public void postProcessEntity(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {	}


	protected Pair<HAPManualBrick, HAPBrick> getBrickPair(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext){
		return HAPManualUtilityBrick.getBrickPair(pathFromRoot, processContext.getCurrentBundle());
	}
}
