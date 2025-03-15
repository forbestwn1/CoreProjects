package com.nosliw.core.application.division.manual.brick.task.flow;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockTaskFlowActivityDynamic extends HAPManualPluginProcessorBlockTaskFlowActivity{

	public HAPManualPluginProcessorBlockTaskFlowActivityDynamic(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.TASK_TASK_ACTIVITYDYNAMIC_100, HAPManualBlockTaskFlowActivityDynamic.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		super.processInit(pathFromRoot, processContext);
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockTaskFlowActivityDynamic definitionBlock = (HAPManualDefinitionBlockTaskFlowActivityDynamic)blockPair.getLeft();
		HAPManualBlockTaskFlowActivityDynamic executableBlock = (HAPManualBlockTaskFlowActivityDynamic)blockPair.getRight();
		
		executableBlock.setRuntime(definitionBlock.getRuntime());
	}

	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		super.processOtherValuePortBuild(pathFromRoot, processContext);
		
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockTaskFlowActivityDynamic definitionBlock = (HAPManualDefinitionBlockTaskFlowActivityDynamic)blockPair.getLeft();
		HAPManualBlockTaskFlowActivityDynamic executableBlock = (HAPManualBlockTaskFlowActivityDynamic)blockPair.getRight();

	}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		super.processBrick(pathFromRoot, processContext);
	}

}
