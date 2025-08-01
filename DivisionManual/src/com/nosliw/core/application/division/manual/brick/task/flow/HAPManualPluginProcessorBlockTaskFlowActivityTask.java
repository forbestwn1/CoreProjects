package com.nosliw.core.application.division.manual.brick.task.flow;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockTaskFlowActivityTask extends HAPManualPluginProcessorBlockTaskFlowActivity{

	public HAPManualPluginProcessorBlockTaskFlowActivityTask(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.TASK_TASK_ACTIVITYTASK_100, HAPManualBlockTaskFlowActivityTask.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		super.processInit(pathFromRoot, processContext);
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockTaskFlowActivityTask definitionBlock = (HAPManualDefinitionBlockTaskFlowActivityTask)blockPair.getLeft();
		HAPManualBlockTaskFlowActivityTask executableBlock = (HAPManualBlockTaskFlowActivityTask)blockPair.getRight();
	}

	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		super.processOtherValuePortBuild(pathFromRoot, processContext);
	}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		super.processBrick(pathFromRoot, processContext);
	}

}
