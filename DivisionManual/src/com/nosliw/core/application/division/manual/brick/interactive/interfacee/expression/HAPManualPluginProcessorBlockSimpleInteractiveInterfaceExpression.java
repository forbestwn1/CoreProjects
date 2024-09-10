package com.nosliw.core.application.division.manual.brick.interactive.interfacee.expression;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockSimpleInteractiveInterfaceExpression extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockSimpleInteractiveInterfaceExpression(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100, HAPManualBlockInteractiveInterfaceExpression.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfoPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockInteractiveInterfaceExpression expressionInteractExe = (HAPManualBlockInteractiveInterfaceExpression)brickInfoPair.getRight();
		HAPManualDefinitionBlockInteractiveInterfaceExpression expressionInteractDef = (HAPManualDefinitionBlockInteractiveInterfaceExpression)brickInfoPair.getLeft();
		expressionInteractExe.setValue(expressionInteractDef.getValue());
	}
}
