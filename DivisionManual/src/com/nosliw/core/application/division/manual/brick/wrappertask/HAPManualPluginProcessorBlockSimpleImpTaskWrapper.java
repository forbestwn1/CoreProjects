package com.nosliw.core.application.division.manual.brick.wrappertask;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.brick.wrappertask.HAPBlockTaskWrapper;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;

public class HAPManualPluginProcessorBlockSimpleImpTaskWrapper extends HAPManualPluginProcessorBlockImp{

	public HAPManualPluginProcessorBlockSimpleImpTaskWrapper() {
		super(HAPEnumBrickType.TASKWRAPPER_100, HAPManualBlockTaskWrapper.class);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfoPair = this.getBrickPair(pathFromRoot, processContext);
		
		HAPIdBrickType taskBrickTypeId = HAPManualDefinitionUtilityBrick.getBrickType(brickInfoPair.getLeft().getAttribute(HAPBlockTaskWrapper.TASK).getValueWrapper());
		
		HAPManualBlockTaskWrapper taskWrapperExe = (HAPManualBlockTaskWrapper)brickInfoPair.getRight();
		taskWrapperExe.setTaskType(HAPUtilityBrick.getBrickTaskType(taskBrickTypeId, processContext.getBrickManager()));
		
	}
}
