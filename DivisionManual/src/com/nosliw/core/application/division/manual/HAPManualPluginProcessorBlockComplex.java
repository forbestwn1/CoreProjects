package com.nosliw.core.application.division.manual;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPManualPluginProcessorBlockComplex extends HAPManualPluginProcessorBlock{

	public HAPManualPluginProcessorBlockComplex(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(brickType, brickClass, runtimeEnv, manualBrickMan);
	}

	//process definition before value context
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	public void postProcessInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}

	//build other value port
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	public void postProcessOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	
	//value context extension, variable resolve
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	public void postProcessVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	
	//matcher
	public void processValueContextDiscovery(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	public void postProcessValueContextDiscovery(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}

	//
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}
	public void postProcessBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {}


	
	

	protected Pair<HAPManualDefinitionBrick, HAPManualBrick> getBrickPair(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext){
		return HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, processContext.getCurrentBundle());
	}
}
