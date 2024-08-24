package com.nosliw.core.application.division.manual.brick.taskwrapper;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.taskwrapper.HAPBlockTaskWrapper;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockSimpleImpTaskWrapper extends HAPManualPluginProcessorBlockSimple{

	public HAPManualPluginProcessorBlockSimpleImpTaskWrapper(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.TASKWRAPPER_100, HAPManualBlockSimpleTaskWrapper.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void process(HAPManualBrick blockExe, HAPManualDefinitionBrick blockDef, HAPManualContextProcessBrick processContext) {
		HAPIdBrickType brickTypeId = HAPManualDefinitionUtilityBrick.getBrickType(blockDef.getAttribute(HAPBlockTaskWrapper.TASK).getValueWrapper());
		
		HAPManualBlockSimpleTaskWrapper taskWrapperExe = (HAPManualBlockSimpleTaskWrapper)blockExe;
		String taskType = processContext.getManualBrickManager().getBrickTypeInfo(brickTypeId).getTaskType();
		taskWrapperExe.setTaskType(taskType);
	}

}
