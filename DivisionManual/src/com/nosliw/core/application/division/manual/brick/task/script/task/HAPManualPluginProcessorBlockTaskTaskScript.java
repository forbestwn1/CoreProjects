package com.nosliw.core.application.division.manual.brick.task.script.task;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;

public class HAPManualPluginProcessorBlockTaskTaskScript extends HAPManualPluginProcessorBlockImp{

	public HAPManualPluginProcessorBlockTaskTaskScript() {
		super(HAPEnumBrickType.TASK_TASK_SCRIPT_100, HAPManualBlockTaskTaskScript.class);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockTaskTaskScript definitionBlock = (HAPManualDefinitionBlockTaskTaskScript)blockPair.getLeft();
		HAPManualBlockTaskTaskScript executableBlock = (HAPManualBlockTaskTaskScript)blockPair.getRight();

		executableBlock.setScriptResourceId(definitionBlock.getScriptResourceId());
		executableBlock.setParms(definitionBlock.getParms());
	}

	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		super.processOtherValuePortBuild(pathFromRoot, processContext);
	}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		
	}

}
