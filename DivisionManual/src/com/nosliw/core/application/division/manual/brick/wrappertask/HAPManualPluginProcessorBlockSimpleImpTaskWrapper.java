package com.nosliw.core.application.division.manual.brick.wrappertask;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.common.task.HAPManualUtilityTask;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.wrappertask.HAPBlockTaskWrapper;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockSimpleImpTaskWrapper extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockSimpleImpTaskWrapper(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.TASKWRAPPER_100, HAPManualBlockTaskWrapper.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfoPair = this.getBrickPair(pathFromRoot, processContext);
		
		HAPIdBrickType brickTypeId = HAPManualDefinitionUtilityBrick.getBrickType(brickInfoPair.getLeft().getAttribute(HAPBlockTaskWrapper.TASK).getValueWrapper());
		
		HAPManualBlockTaskWrapper taskWrapperExe = (HAPManualBlockTaskWrapper)brickInfoPair.getRight();
		taskWrapperExe.setTaskType(HAPManualUtilityTask.getBrickTaskType(brickTypeId, processContext.getManualBrickManager()));
	}
}
