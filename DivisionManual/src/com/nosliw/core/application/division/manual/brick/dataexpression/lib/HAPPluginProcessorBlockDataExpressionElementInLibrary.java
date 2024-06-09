package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.lib.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.common.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimpleImp;
import com.nosliw.data.core.dataexpression.HAPExecutableExpressionData;
import com.nosliw.data.core.operand.HAPOperand;
import com.nosliw.data.core.operand.HAPWrapperOperand;

public class HAPPluginProcessorBlockDataExpressionElementInLibrary extends HAPPluginProcessorBlockSimpleImp{

	public HAPPluginProcessorBlockDataExpressionElementInLibrary(String entityType) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100);
	}

	@Override
	public void process(HAPBrickBlockSimple blockExe, HAPManualBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext) {
		HAPBlockDataExpressionElementInLibrary eleExe = (HAPBlockDataExpressionElementInLibrary)blockExe;
		HAPManualBlockDataExpressionElementInLibrary eleDef = (HAPManualBlockDataExpressionElementInLibrary)blockDef;
		
		//build expression in executable
		HAPOperand operand = processContext.getRuntimeEnv().getDataExpressionParser().parseExpression(eleDef.getExpression());
		eleExe.setExpression(new HAPExecutableExpressionData(new HAPWrapperOperand(operand)));

		//resolve variable name
		ref.setValuePortId(HAPUtilityValuePort.normalizeExternalValuePortId(ref.getValuePortId(), pathFromRoot, bundle, processContext.getRuntimeEnv().getResourceManager(), processContext.getRuntimeEnv().getRuntime().getRuntimeInfo()));
		HAPResultReferenceResolve resolve  = HAPUtilityStructureElementReference.analyzeElementReference(ref, new HAPConfigureResolveElementReference(), bundle, processContext.getRuntimeEnv().getResourceManager(), processContext.getRuntimeEnv().getRuntime().getRuntimeInfo());
		
		
		//process reference
		
		
		//discover
		
	}

}
