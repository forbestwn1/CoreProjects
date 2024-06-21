package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.lib.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.common.dataexpression.HAPDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPDataExpressionElementInLibrary;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.HAPWrapperOperand;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimpleImp;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPUtilityExpressionProcessor;

public class HAPPluginProcessorBlockDataExpressionElementInLibrary extends HAPPluginProcessorBlockSimpleImp{

	public HAPPluginProcessorBlockDataExpressionElementInLibrary() {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100);
	}

	@Override
	public void process(HAPBrickBlockSimple blockExe, HAPManualBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext) {
		HAPDataExpressionElementInLibrary exe = ((HAPBlockDataExpressionElementInLibrary)blockExe).getValue();;
		HAPManualDataExpressionLibraryElement def = ((HAPManualBlockDataExpressionElementInLibrary)blockDef).getValue();
		
		def.cloneToEntityInfo(exe);
		
		//build expression in executable
		HAPOperand operand = processContext.getRuntimeEnv().getDataExpressionParser().parseExpression(def.getExpression());
		exe.setExpression(new HAPDataExpression(new HAPWrapperOperand(operand)));

		//
		exe.setInteractive(new HAPInteractiveExpression(def.getRequestParms(), def.getResult()));
		
		//resolve variable name
		HAPUtilityExpressionProcessor.resolveVariableName(exe.getExpression(), blockExe, exe.getVariablesInfo());
		
		//process reference
		HAPUtilityExpressionProcessor.resolveReferenceVariableMapping(exe.getExpression(), processContext.getRuntimeEnv());
		
		//discover
		
	}

}
