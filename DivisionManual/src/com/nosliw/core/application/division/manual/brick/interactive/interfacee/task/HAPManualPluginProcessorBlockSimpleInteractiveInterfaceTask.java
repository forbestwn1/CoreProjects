package com.nosliw.core.application.division.manual.brick.interactive.interfacee.task;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockSimpleInteractiveInterfaceTask extends HAPManualPluginProcessorBlockImp{

	public HAPManualPluginProcessorBlockSimpleInteractiveInterfaceTask(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.INTERACTIVETASKINTERFACE_100, HAPManualBlockInteractiveInterfaceTask.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfoPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockInteractiveInterfaceTask taskInteractTask = (HAPManualBlockInteractiveInterfaceTask)brickInfoPair.getRight();
		HAPManualDefinitionBlockInteractiveInterfaceTask taskInteractTaskDef = (HAPManualDefinitionBlockInteractiveInterfaceTask)brickInfoPair.getLeft();
		taskInteractTask.setValue(taskInteractTaskDef.getValue());
	}
}
