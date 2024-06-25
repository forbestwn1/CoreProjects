package com.nosliw.core.application.division.manual.brick.taskwrapper;

import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.brick.taskwrapper.HAPBlockTaskWrapper;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualUtilityBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimpleImp;

public class HAPManualPluginProcessorBlockSimpleImpTaskWrapper extends HAPPluginProcessorBlockSimpleImp{

	public HAPManualPluginProcessorBlockSimpleImpTaskWrapper() {
		super(HAPEnumBrickType.TASKWRAPPER_100);
	}

	@Override
	public void process(HAPBrickBlockSimple blockExe, HAPManualBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext) {
		HAPIdBrickType brickTypeId = HAPManualUtilityBrick.getBrickType(blockDef.getAttribute(HAPBlockTaskWrapper.TASK).getValueWrapper());
		String taskType = processContext.getRuntimeEnv().getBrickManager().getBrickTypeInfo(brickTypeId).getTaskType();
		
		HAPBlockTaskWrapper taskWrapperExe = (HAPBlockTaskWrapper)blockExe;
		taskWrapperExe.setTaskType(taskType);
	}

}
