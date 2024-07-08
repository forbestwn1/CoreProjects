package com.nosliw.core.application.division.manual.brick.taskwrapper;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.taskwrapper.HAPBlockTaskWrapper;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;

public class HAPManualPluginProcessorBlockSimpleImpTaskWrapper extends HAPPluginProcessorBlockSimple{

	public HAPManualPluginProcessorBlockSimpleImpTaskWrapper() {
		super(HAPEnumBrickType.TASKWRAPPER_100);
	}

	@Override
	public void process(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext) {
		HAPIdBrickType brickTypeId = HAPManualDefinitionUtilityBrick.getBrickType(blockDef.getAttribute(HAPBlockTaskWrapper.TASK).getValueWrapper());
		String taskType = processContext.getRuntimeEnv().getBrickManager().getBrickTypeInfo(brickTypeId).getTaskType();
		
		HAPBlockTaskWrapper taskWrapperExe = (HAPBlockTaskWrapper)blockExe;
		taskWrapperExe.setTaskType(taskType);
	}

}
