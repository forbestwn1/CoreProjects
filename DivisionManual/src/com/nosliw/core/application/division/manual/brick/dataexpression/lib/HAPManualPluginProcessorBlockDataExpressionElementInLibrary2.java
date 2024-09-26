package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.library.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.brick.dataexpression.library.HAPElementInLibraryDataExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.valueport.HAPIdElement;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.application.common.valueport.HAPVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockSimple;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualExpressionData;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualOperand;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualUtilityOperand;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualUtilityProcessorDataExpression;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualWrapperOperand;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockDataExpressionElementInLibrary2 extends HAPManualPluginProcessorBlockSimple{

	public HAPManualPluginProcessorBlockDataExpressionElementInLibrary2(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, HAPManualBlockDataExpressionElementInLibrary.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void process(HAPManualBrick blockExe, HAPManualDefinitionBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext) {
		HAPElementInLibraryDataExpression exe = ((HAPBlockDataExpressionElementInLibrary)blockExe).getValue();;
		HAPManualDataExpressionLibraryElement def = ((HAPManualDefinitionBlockDataExpressionElementInLibrary)blockDef).getValue();
		
		def.cloneToEntityInfo(exe);
		
		//build expression in executable
		exe.setExpression(new HAPManualExpressionData(HAPManualUtilityProcessorDataExpression.buildManualOperand(def.getExpression().getOperand())));
		HAPManualExpressionData dataExpression = (HAPManualExpressionData)exe.getExpression();
		
		//interactive request
		exe.setExpressionInteractive(new HAPInteractiveExpression(def.getRequestParms(), def.getResult()));
		
		HAPManualWrapperOperand operandWrapper = dataExpression.getOperandWrapper();
		
		//resolve variable name, build var info container
		HAPContainerVariableInfo varInfoContainer = new HAPContainerVariableInfo(blockExe);
		HAPManualUtilityProcessorDataExpression.resolveVariable(dataExpression, varInfoContainer, null);
		
		Map<String, HAPIdElement> varInfos = varInfoContainer.getVariables();
		for(String key : varInfos.keySet()) {
			dataExpression.setVariableInfo(new HAPVariableInfo(key, varInfos.get(key))); 
		}
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, blockExe);

		//process reference operand
		HAPManualUtilityProcessorDataExpression.resolveReferenceVariableMapping(dataExpression, processContext.getRuntimeEnv());

		//discover
		List<HAPManualOperand> operands = new ArrayList<HAPManualOperand>();
		operands.add(operandWrapper.getOperand());
		List<HAPDataTypeCriteria> expectOutputs = new ArrayList<HAPDataTypeCriteria>();
		expectOutputs.add(exe.getExpressionInterface().getResult().getDataCriteria());
		List<HAPMatchers> matchers = new ArrayList<HAPMatchers>();
		varInfoContainer = HAPManualUtilityOperand.discover(
				operands,
				expectOutputs,
				varInfoContainer,
				matchers,
				processContext.getRuntimeEnv().getDataTypeHelper(),
				new HAPProcessTracker());
		
		//update value port element according to var info container after resolve
		HAPUtilityValuePortVariable.updateValuePortElements(varInfoContainer, blockExe);
		
		//result
		HAPDataTypeCriteria resultCriteria = operandWrapper.getOperand().getOutputCriteria();
		if(exe.getExpressionInterface().getResult().getDataCriteria()==null) {
			exe.getExpressionInterface().getResult().setDataCriteria(resultCriteria);
		}
		else {
			exe.setResultMatchers(matchers.get(0));
		}
	}
		
}
