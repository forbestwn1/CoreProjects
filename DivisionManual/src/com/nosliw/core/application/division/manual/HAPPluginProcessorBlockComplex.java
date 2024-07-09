package com.nosliw.core.application.division.manual;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginProcessorBlockComplex extends HAPPluginProcessorBlock{

	public HAPPluginProcessorBlockComplex(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(brickType, brickClass, runtimeEnv, manualBrickMan);
	}


	//process definition before value context
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	public void postProcessInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}

	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	public void postProcessBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}


	
	
	
	
	public void extendConstantValue(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	

	
	
	//value context extension, variable resolve
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	public void postProcessVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	
	//matcher
	public void processValueContextDiscovery(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	public void postProcessValueContextDiscovery(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {}
	
	public void processEntity(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {	}
	public void postProcessEntity(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext) {	}


	protected Pair<HAPManualDefinitionBrick, HAPManualBrick> getBrickPair(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext){
		return HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, processContext.getCurrentBundle());
	}
}
