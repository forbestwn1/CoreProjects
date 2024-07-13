package com.nosliw.core.application.division.manual.brick.dataexpression.lib;

import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.brick.dataexpression.library.HAPBlockDataExpressionElementInLibrary;
import com.nosliw.core.application.brick.dataexpression.library.HAPElementInLibraryDataExpression;
import com.nosliw.core.application.common.interactive.HAPInteractiveExpression;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePortVariable;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.common.withvariable.HAPUtilityWithVarible;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimple;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualDataExpression;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualUtilityProcessorDataExpression;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPManualWrapperOperand;
import com.nosliw.core.application.division.manual.common.dataexpression.HAPPluginProcessorEntityWithVariableDataExpression;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginProcessorBlockDataExpressionElementInLibrary extends HAPPluginProcessorBlockSimple{

	public HAPPluginProcessorBlockDataExpressionElementInLibrary(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.DATAEXPRESSIONLIBELEMENT_100, HAPManualBlockDataExpressionElementInLibrary.class, runtimeEnv, manualBrickMan);
	}

	@Override
	public void process(HAPManualBrick blockExe, HAPManualDefinitionBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext) {
		HAPElementInLibraryDataExpression exe = ((HAPBlockDataExpressionElementInLibrary)blockExe).getValue();;
		HAPManualDataExpressionLibraryElement def = ((HAPManualDefinitionBlockDataExpressionElementInLibrary)blockDef).getValue();
		
		def.cloneToEntityInfo(exe);
		
		//build expression in executable
		exe.setExpression(new HAPManualDataExpression(HAPManualUtilityProcessorDataExpression.buildManualOperand(def.getExpression().getOperand())));
		HAPManualDataExpression dataExpression = (HAPManualDataExpression)exe.getExpression();
		
		//interactive request
		exe.setInteractive(new HAPInteractiveExpression(def.getRequestParms(), def.getResult()));
		
		HAPManualWrapperOperand operandWrapper = dataExpression.getOperandWrapper();
		
		HAPContainerVariableInfo varInfoContainer = new HAPContainerVariableInfo(blockExe);

		//resolve variable name, build var info container
		HAPUtilityWithVarible.resolveVariable(dataExpression, varInfoContainer, null, getManualBrickManager());
		
		//build variable info in data expression
		HAPUtilityWithVarible.buildVariableInfoInEntity(dataExpression, varInfoContainer, getManualBrickManager());
		
		//build var criteria infor in var info container according to value port def
		HAPUtilityValuePortVariable.buildVariableInfo(varInfoContainer, blockExe);

		//discover
		Map<String, HAPDataTypeCriteria> expections = new LinkedHashMap<String, HAPDataTypeCriteria>();
		expections.put(HAPPluginProcessorEntityWithVariableDataExpression.RESULT, exe.getInteractive().getResult().getDataCriteria());
		Pair<HAPContainerVariableInfo, Map<String, HAPMatchers>> discoverResult = HAPUtilityWithVarible.discoverVariableCriteria(dataExpression, expections, varInfoContainer, getManualBrickManager());
		varInfoContainer = discoverResult.getLeft();
		
		//update value port element according to var info container after discovery
		HAPUtilityValuePortVariable.updateValuePortElements(varInfoContainer, blockExe);
		
		//result
		HAPDataTypeCriteria resultCriteria = operandWrapper.getOperand().getOutputCriteria();
		if(exe.getInteractive().getResult().getDataCriteria()==null) {
			exe.getInteractive().getResult().setDataCriteria(resultCriteria);
		}
		else {
			exe.setResultMatchers(discoverResult.getRight().get(HAPPluginProcessorEntityWithVariableDataExpression.RESULT));
		}
	}
		
}
