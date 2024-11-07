package com.nosliw.core.application.division.manual.brick.task.script.task;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPUtilityBrick;
import com.nosliw.core.application.brick.interactive.interfacee.task.HAPBlockInteractiveInterfaceTask;
import com.nosliw.core.application.common.interactive.HAPInteractiveTask;
import com.nosliw.core.application.common.interactive.HAPUtilityInteractiveValuePort;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockTaskTaskScript extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockTaskTaskScript(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.TASK_TASK_SCRIPT_100, HAPManualBlockTaskTaskScript.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, bundle);
		HAPManualDefinitionBlockTaskTaskScript definitionBlock = (HAPManualDefinitionBlockTaskTaskScript)blockPair.getLeft();
		HAPManualBlockTaskTaskScript executableBlock = (HAPManualBlockTaskTaskScript)blockPair.getRight();

		executableBlock.setScriptResourceId(definitionBlock.getScriptResourceId());
	}

	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		HAPBundle bundle = processContext.getCurrentBundle();
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = HAPManualDefinitionUtilityBrick.getBrickPair(pathFromRoot, bundle);
		HAPManualDefinitionBlockTaskTaskScript definitionBlock = (HAPManualDefinitionBlockTaskTaskScript)blockPair.getLeft();
		HAPManualBlockTaskTaskScript executableBlock = (HAPManualBlockTaskTaskScript)blockPair.getRight();

		//build value port group
		HAPEntityOrReference taskInterface = executableBlock.getTaskInterface();
		if(taskInterface!=null) {
			HAPBlockInteractiveInterfaceTask taskInterfaceBlock = (HAPBlockInteractiveInterfaceTask)HAPUtilityBrick.getBrick(taskInterface, processContext.getRuntimeEnv().getBrickManager());
			HAPInteractiveTask taskInteractive = taskInterfaceBlock.getValue();
			Pair<HAPGroupValuePorts, HAPGroupValuePorts> valuePortGroupPair = HAPUtilityInteractiveValuePort.buildValuePortGroupForInteractiveTask(taskInteractive, processContext.getCurrentBundle().getValueStructureDomain());
			executableBlock.addOtherInternalValuePortGroup(valuePortGroupPair.getLeft());
			executableBlock.addOtherExternalValuePortGroup(valuePortGroupPair.getRight());
		}
	}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		
	}

}
