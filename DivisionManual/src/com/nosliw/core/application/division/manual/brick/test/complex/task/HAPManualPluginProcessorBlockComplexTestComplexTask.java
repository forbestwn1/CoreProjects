package com.nosliw.core.application.division.manual.brick.test.complex.task;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockComplexTestComplexTask extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockComplexTestComplexTask(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.TEST_COMPLEX_TASK_100, HAPManualBlockTestComplexTask.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, bundle);
		HAPManualDefinitionBlockTestComplexTask definitionBlock = (HAPManualDefinitionBlockTestComplexTask)blockPair.getLeft();
		HAPManualBlockTestComplexTask executableBlock = (HAPManualBlockTestComplexTask)blockPair.getRight();

	}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		
		HAPBundle bundle = processContext.getCurrentBundle();
		
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, bundle);
		HAPManualDefinitionBlockTestComplexTask definitionBlock = (HAPManualDefinitionBlockTestComplexTask)blockPair.getLeft();
		HAPManualBlockTestComplexTask executableBlock = (HAPManualBlockTestComplexTask)blockPair.getRight();
		
	}
	
}
