package com.nosliw.core.application.division.manual.brick.interactive.interfacee.expression;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockImp;

public class HAPManualPluginProcessorBlockSimpleInteractiveInterfaceExpression extends HAPManualPluginProcessorBlockImp{

	public HAPManualPluginProcessorBlockSimpleInteractiveInterfaceExpression() {
		super(HAPEnumBrickType.INTERACTIVEEXPRESSIONINTERFACE_100, HAPManualBlockInteractiveInterfaceExpression.class);
	}

	@Override
	public void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext) {
		Pair<HAPManualDefinitionBrick, HAPManualBrick> brickInfoPair = this.getBrickPair(pathFromRoot, processContext);
		HAPManualBlockInteractiveInterfaceExpression expressionInteractExe = (HAPManualBlockInteractiveInterfaceExpression)brickInfoPair.getRight();
		HAPManualDefinitionBlockInteractiveInterfaceExpression expressionInteractDef = (HAPManualDefinitionBlockInteractiveInterfaceExpression)brickInfoPair.getLeft();
		expressionInteractExe.setValue(expressionInteractDef.getValue());
	}
}
