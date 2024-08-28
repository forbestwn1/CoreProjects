package com.nosliw.core.application.division.manual.brick.taskwrapper;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.taskwrapper.HAPBlockTaskWrapper;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockSimpleImpTaskWrapper extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockSimpleImpTaskWrapper(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.TASKWRAPPER_100, HAPManualBlockSimpleTaskWrapper.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfoPair = this.getBrickPair(pathFromRoot, processContext);
		
		HAPIdBrickType brickTypeId = HAPManualDefinitionUtilityBrick.getBrickType(brickInfoPair.getLeft().getAttribute(HAPBlockTaskWrapper.TASK).getValueWrapper());
		
		HAPManualBlockSimpleTaskWrapper taskWrapperExe = (HAPManualBlockSimpleTaskWrapper)brickInfoPair.getRight();
		String taskType = processContext.getManualBrickManager().getBrickTypeInfo(brickTypeId).getTaskType();
		taskWrapperExe.setTaskType(taskType);
	}
}
