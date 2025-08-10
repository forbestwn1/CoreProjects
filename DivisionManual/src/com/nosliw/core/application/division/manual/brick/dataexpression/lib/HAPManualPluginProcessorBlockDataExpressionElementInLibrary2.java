package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicExpressionData;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicUtilityOperand;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicUtilityProcessorDataExpression;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicWrapperOperand;
import com.nosliw.core.application.common.dataexpression.imp.basic.HAPBasicOperand;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualContextProcessBrick;
import com.nosliw.core.application.valueport.HAPIdElement;
import com.nosliw.core.application.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.xxx.application.division.manual.core.a.HAPManualPluginProcessorBlockSimple;
import com.nosliw.core.xxx.application.valueport.HAPVariableInfo;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.core.xxx.application1.brick.dataexpression.library.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.xxx.application1.brick.dataexpression.library.HAPElementInLibraryDataExpression;
import com.nosliw.core.xxx.application1.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
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
		exe.setExpression(new HAPBasicExpressionData(HAPBasicUtilityProcessorDataExpression.buildBasicOperand(def.getExpression().getOperand())));
		HAPBasicExpressionData dataExpression = (HAPBasicExpressionData)exe.getExpression();
		
		//interactive request
		exe.setExpressionInteractive(new HAPInteractiveExpression(def.getRequestParms(), def.getResult()));
		
		HAPBasicWrapperOperand operandWrapper = dataExpression.getOperandWrapper();
		
		//resolve variable name, build var info container
		HAPContainerVariableInfo varInfoContainer = new HAPContainerVariableInfo(blockExe);
		HAPBasicUtilityProcessorDataExpression.resolveVariable(dataExpression, varInfoContainer, null);
		
		Map<String, HAPIdElement> varInfos = varInfoContainer.getVariables();
		for(String key : varInfos.keySet()) {
			dataExpression.setVariableInfo(new HAPVariableInfo(key, varInfos.get(key))); 
		}
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, blockExe);

		//process reference operand
		HAPBasicUtilityProcessorDataExpression.resolveReferenceVariableMapping(dataExpression, processContext.getRuntimeEnv());

		//discover
		List<HAPBasicOperand> operands = new ArrayList<HAPBasicOperand>();
		operands.add(operandWrapper.getOperand());
		List<HAPDataTypeCriteria> expectOutputs = new ArrayList<HAPDataTypeCriteria>();
		expectOutputs.add(exe.getExpressionInterface().getResult().getDataCriteria());
		List<HAPMatchers> matchers = new ArrayList<HAPMatchers>();
		varInfoContainer = HAPBasicUtilityOperand.discover(
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
