package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.lib.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.common.dataexpression.HAPContainerVariableCriteriaInfo;
import com.nosliw.core.application.common.dataexpression.HAPDataExpression;
import com.nosliw.core.application.common.dataexpression.HAPDataExpressionElementInLibrary;
import com.nosliw.core.application.common.dataexpression.HAPOperand;
import com.nosliw.core.application.common.dataexpression.HAPUtilityOperand;
import com.nosliw.core.application.common.dataexpression.HAPWrapperOperand;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimpleImp;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPUtilityExpressionProcessor;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;

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
		
		//resolve variable name, build var info container
		HAPUtilityExpressionProcessor.resolveVariableName(exe.getExpression(), blockExe, exe.getVariablesInfo(), null);
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityExpressionProcessor.buildVariableInfo(exe.getVariablesInfo(), blockExe);
		
		//process reference operand
		HAPUtilityExpressionProcessor.resolveReferenceVariableMapping(exe.getExpression(), processContext.getRuntimeEnv());
		
		//discover
		List<HAPOperand> operands = new ArrayList<HAPOperand>();
		operands.add(exe.getExpression().getOperand().getOperand());
		List<HAPDataTypeCriteria> expectOutputs = new ArrayList<HAPDataTypeCriteria>();
		expectOutputs.add(exe.getInteractive().getResult().getDataCriteria());
		List<HAPMatchers> matchers = new ArrayList<HAPMatchers>();
		HAPContainerVariableCriteriaInfo variableInfos = HAPUtilityOperand.discover(
				operands,
				expectOutputs,
				exe.getVariablesInfo(),
				matchers,
				processContext.getRuntimeEnv().getDataTypeHelper(),
				new HAPProcessTracker());
		exe.setVariablesInfo(variableInfos);
		
		//update value port element according to var info container after resolve
		HAPUtilityExpressionProcessor.updateValuePortElements(exe.getVariablesInfo(), blockExe);
		
		//result
		HAPDataTypeCriteria resultCriteria = exe.getExpression().getOperand().getOperand().getOutputCriteria();
		if(exe.getInteractive().getResult().getDataCriteria()==null) {
			exe.getInteractive().getResult().setDataCriteria(resultCriteria);
		}
		else {
			exe.setResultMatchers(matchers.get(0));
		}
	}
}