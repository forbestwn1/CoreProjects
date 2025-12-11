package com.nosliw.core.application.division.manual.brick.test.complex.task.script;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.common.withvariable.HAPVariableInfo;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;
import com.nosliw.core.application.valueport.HAPIdElement;
import com.nosliw.core.application.valueport.HAPUtilityResovleElement;

public class HAPManualPluginProcessorBlockTestComplexTaskScript extends HAPManualPluginProcessorBlockImp{

	public HAPManualPluginProcessorBlockTestComplexTaskScript() {
		super(HAPEnumBrickType.TEST_COMPLEX_TASK_SCRIPT_100, HAPManualBlockTestComplexTaskScript.class);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockTestComplexTaskScript definitionBlock = (HAPManualDefinitionBlockTestComplexTaskScript)blockPair.getLeft();
		HAPManualBlockTestComplexTaskScript executableBlock = (HAPManualBlockTestComplexTaskScript)blockPair.getRight();

	}

	@Override
	public void processOtherValuePortBuild(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		super.processOtherValuePortBuild(pathFromRoot, processContext);
	}

	@Override
	public void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockTestComplexTaskScript definitionBlock = (HAPManualDefinitionBlockTestComplexTaskScript)blockPair.getLeft();
		HAPManualBlockTestComplexTaskScript executableBlock = (HAPManualBlockTestComplexTaskScript)blockPair.getRight();

		//script
		executableBlock.setScriptResourceId(definitionBlock.getScriptResourceId());
		
		//interactive interface
		executableBlock.setTaskInterface(definitionBlock.getTaskInterface());
		executableBlock.setExpressionInterface(definitionBlock.getExpressionInterface());
		
	}

	@Override
	public void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> blockPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualDefinitionBlockTestComplexTaskScript definitionBlock = (HAPManualDefinitionBlockTestComplexTaskScript)blockPair.getLeft();
		HAPManualBlockTestComplexTaskScript executableBlock = (HAPManualBlockTestComplexTaskScript)blockPair.getRight();

		//resolve variable
		for(String variableName : definitionBlock.getVariables()) {
			HAPIdElement varElementId = HAPUtilityResovleElement.resolveNameFromInternal(variableName, null, executableBlock, null, processContext.getCurrentBundle().getValueStructureDomain()).getElementId();
			executableBlock.addVariable(variableName, new HAPVariableInfo(variableName, varElementId));
		}
	}

}
