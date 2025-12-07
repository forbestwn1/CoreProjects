package com.nosliw.core.application.division.manual.core.process;

import com.nosliw.core.application.HAPWrapperBrickRoot;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.core.application.division.manual.core.HAPManualWrapperBrickRoot;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionWrapperBrickRoot;
import com.nosliw.core.runtime.HAPRuntimeManager;

public class HAPManualProcessBrick {

	public static HAPWrapperBrickRoot processRootBrick(HAPManualDefinitionWrapperBrickRoot rootBrickDefWrapper, HAPRuntimeManager runtimeMan, HAPParserDataExpression dataExpressionParser, HAPManualContextProcessBrick processContext) {
		//process definition first
		HAPManualUtilityProcessorPre.process(rootBrickDefWrapper, runtimeMan, dataExpressionParser, processContext);
		
		//init brick
		HAPManualWrapperBrickRoot out = HAPManualProcessProcessorInit.process(rootBrickDefWrapper, processContext);
		
		HAPManualUtilityProcessBrickPath.processComplexBrickNormalizeBrickPath(processContext);
		
		//process value port
		HAPManualUtilityProcessorValuePort.process(processContext);
		
		//process variable
		HAPManualUtilityProcessorVariable.process(processContext);
		
		//process brick
		HAPManualUtilityProcessorBrick.process(processContext);
		
		//process adapter
		HAPManualUtilityProcessorAdapter.processAdapterInAttribute(processContext);
		
		//post process
		HAPManualUtilityProcessorPost.process(processContext);
		
		return out;
	}
}
