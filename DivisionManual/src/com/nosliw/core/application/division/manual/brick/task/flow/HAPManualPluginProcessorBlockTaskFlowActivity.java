package com.nosliw.core.application.division.manual.brick.task.flow;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockTaskFlowActivity extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockTaskFlowActivity(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(brickType, brickClass, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockTaskFlowActivity definitionBlock = (HAPManualDefinitionBlockTaskFlowActivity)blockPair.getLeft();
		HAPManualBlockTaskFlowActivity executableBlock = (HAPManualBlockTaskFlowActivity)blockPair.getRight();

		executableBlock.setNext(definitionBlock.getNext());
		definitionBlock.cloneToEntityInfo(executableBlock);
	}

	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		super.processOtherValuePortBuild(pathFromRoot, processContext);
	}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		
	}

}
