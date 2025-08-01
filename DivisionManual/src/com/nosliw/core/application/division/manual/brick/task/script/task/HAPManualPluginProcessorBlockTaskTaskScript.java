package com.nosliw.core.application.division.manual.brick.task.script.task;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockTaskTaskScript extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockTaskTaskScript(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.TASK_TASK_SCRIPT_100, HAPManualBlockTaskTaskScript.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockTaskTaskScript definitionBlock = (HAPManualDefinitionBlockTaskTaskScript)blockPair.getLeft();
		HAPManualBlockTaskTaskScript executableBlock = (HAPManualBlockTaskTaskScript)blockPair.getRight();

		executableBlock.setScriptResourceId(definitionBlock.getScriptResourceId());
	}

	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		super.processOtherValuePortBuild(pathFromRoot, processContext);
	}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		
	}

}
