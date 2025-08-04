package com.nosliw.core.application.division.manual.brick.interactive.interfacee.expression;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
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
